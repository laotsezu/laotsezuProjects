<?php 
	class ModelTradeBill extends BaseModel{
		function __construct($input){
			
			parent::__construct($input);

		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"bill_id"
			,"bill_thoi_gian"
			,"customer_id"
			,"agency_id"
			,"personnel_id"
			,"bill_ghi_chu"
			,"bill_tong_tien_hang"
			,"bill_giam_gia"
			,"bill_tien_can_tra"
			,"bill_tien_da_tra"
			,"bill_status"
			);
		}
		function getEnglishName(){
			return "bill";
		}
		function getTableName(){
			return "hoa_don";
		}
		function getBillById($bill_id){
			$this->sql_model->where("bill_id",$bill_id);
			$bill = $this->sql_model->getOne(null);
			
			if(!$bill){
				throw new Exception(self::$TAG."Bill with id = " . $bill_id ."is not exist!", 1);
			}
			else{
				return $bill;
			}
		}
	

		function payment(){
			$goodses = $this->explodeGoodsIds($this->getPropertyValue("current_goods_ids"));
			$this->unSetData("current_goods_ids");
			///begin transaction 
			try{
				$this->sql_model->startTransaction();
				//update khach hang
					$customer_id = $this->getPropertyValue("customer_id");
					if(!$customer_id)$customer_id = 0;
					$customer = $this->model("ModelPartnerCustomer",array("customer_id" => $customer_id),"partner");
				//	$customer->beginTransaction();
					$owe = $this->getPropertyValue("bill_tien_can_tra") - $this->getPropertyValue("bill_tien_da_tra");
					if($owe){
						$customer->increaseOwe($owe);
					}
				
				//update nguoi ban 
				$personnel = $this->model("ModelPersonnelPersonnel",array("personnel_id" => $this->getPropertyValue("personnel_id")),"personnel");
			//	$personnel->beginTransaction();

				if($this->getPropertyValue("bill_tien_da_tra")){
					$personnel->increaseTotalSell($this->getPropertyValue("bill_tien_da_tra"));
					
					$customer->increaseTotalBuy($this->getPropertyValue("bill_tien_da_tra"));
				}

				//them hoa don
				$this->insert();
				////update hang hoa
				$id = $this->getLastInsertId();
				$goods = $this->model("ModelRepositoryGoods",null,"repository");
				$goods_and_bill = $this->model("ModelTradeGoodsAndBill",null,"trade");

				for($i = 0;$i < count($goodses);$i++){
					///update so luong hang hoa
					$goods_data = $goodses[$i];
					$goods->setData($goods_data);
					$goods->decreaseAmount($goods_data["goods_so_luong"]);
					/////them goods _and _ bill
					$goods_and_bill_data = array();
					
					$goods_and_bill_data["gab_bill_id"] = $id;
					$goods_and_bill_data["gab_goods_id"] = $goods_data["goods_id"];
					$goods_and_bill_data["gab_origin_so_luong"] = $goods_data["goods_so_luong"];
					$goods_and_bill_data["gab_current_so_luong"] = $goods_data["goods_so_luong"];
					$goods_and_bill_data["gab_goods_gia_ban"] = $goods->getPropertyValueFromDb("goods_gia_ban");
					$goods_and_bill_data["gab_goods_gia_von"] = $goods->getPropertyValueFromDb("goods_gia_von");

					$goods_and_bill->setData($goods_and_bill_data);
					$goods_and_bill->add();
				}

				$this->sql_model->commit();

				return true;
			}
			catch(Exception $e){
				$this->sql_model->rollback();
				
				$this->throwException($e->getMessage());
			}
		}
	
			/*function decreaseGoodsIds($goods_ids){
			$goodses = $this->explodeGoodsIds($goods_ids);
			$old_goodses = $this->explodeGoodsIds($this->getPropertyValueFromDb("current_goods_ids"));
			$result = "";
			for($i = 0; $i < count($goodses) ; $i++){
				$result.= $goodses[$i]["goods_id"]."=";
				$new_amount = $old_goodses[$i]["goods_so_luong"] - $goodses[$i]["goods_so_luong"];
				
				if(!$new_amount)
					throw new Exception("Decrease Goods Ids Failed : new amount < 0", 1);
					
				$result.= $new_amount;
				$result.= ",";
			}
			$result = substr($result,0,strlen($result) - 2);
			
			$this->update(array("current_goods_ids" => $result));
		}*/

	}

	
?>