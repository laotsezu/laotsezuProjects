<?php 
	class ModelTradeRBill extends BaseModel{

		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"rbill_id"
			,"rbill_thoi_gian"
			,"bill_id"
			,"rbill_goods_ids"
			,"customer_id"
			,"agency_id"
			,"personnel_id"
			,"rbill_ghi_chu"
			,"rbill_tong_tien_tra"
			,"rbill_phi_tra_hang"
			,"rbill_giam_gia"
			,"rbill_tien_tra_khach"
			,"rbill_status"
			);
		}
		function getEnglishName(){
			return "rbill";
		}
		function getTableName(){
			return "hoa_don_tra_hang";
		}
		function accept(){
			//echo "Bat dau tra hang: <Br>";
			$goodses = $this->explodeGoodsIds($this->getPropertyValue("rbill_goods_ids"));
			try{
				$this->sql_model->startTransaction();

				//Lay customer_id và personnel_id
				$bill= $this->model("ModelTradeBill",array("bill_id" => $this->getPropertyValue("bill_id")),"trade");
				$bill_data = $bill->getBillById($this->getPropertyValue("bill_id"));
				//update nguoi ban 
				$personnel = $this->model("ModelPersonnelPersonnel",array("personnel_id" => $bill_data["personnel_id"]),"personnel");
				$personnel->decreaseTotalSell($this->getPropertyValue("rbill_tien_tra_khach"));
				//update khach hang
				$customer = $this->model("ModelPartnerCustomer",array("customer_id" => $bill_data["customer_id"]),"partner");
				$customer->decreaseTotalBuy($this->getPropertyValue("rbill_tien_tra_khach"));				
				////insert rbill
				$this->insert();
				$goods = $this->model("ModelRepositoryGoods",null,"repository");
				$goods_and_bill = $this->model("ModelTradeGoodsAndBill",null,"trade")
				////update hang hoa
				for($i = 0;$i < count($goodses);$i++){
					//giam so luonghang hoa 
					$goods_data = $goodses[$i];
					$goods->setData($goods_data);
					$goods->increaseAmount($goods->getPropertyValue("goods_so_luong"));

					//cap nhat lai so luong trong goods and _bill
					$goods_and_bill_data = array();

					$goods_and_bill_data["bill_id"] = $this->getPropertyValue("bill_id");
					$goods_and_bill_data["goods_id"] = $goods_data["goods_id"];

					$goods_and_bill->setData($goods_and_bill_data);
					$goods_and_bill->decreaseAmount($goods_data["goods_so_luong"]);
				}

				$this->sql_model->commit();

				return true;
			}
			catch(Exception $e){
				$this->sql_model->rollback();
				throw new Exception($e->getMessage(), 1);
			}
		}	
	}
?>