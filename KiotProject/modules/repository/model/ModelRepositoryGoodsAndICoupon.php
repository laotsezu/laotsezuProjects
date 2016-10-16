<?php 
	class ModelRepositoryGoodsAndICoupon extends BaseModel{
		static $TAG = "ModelRepositoryGoodsAndICoupon: ";
		function __construct($input){
			if((isset($input["gaic_current_so_luong"])) && !(isset($input["gaic_origin_so_luong"]))){
				$input["gaic_origin_so_luong"] = $input["gaic_current_so_luong"];
			}
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"gaic_icoupon_id"
			,"gaic_goods_id"
			,"gaic_origin_so_luong"
			,"gaic_current_so_luong"
			,"gaic_goods_gia_ban"
			,"gaic_tong_tien_ban"
			,"gaic_thoi_gian"
			,"gaic_status"
			);
		}
		function getEnglishName(){
			return "gaic";
		}
		function getTableName(){
			return "hang_hoa_va_hoa_don";
		}
		private function setWhere(){
			$this->sql_model->where("gaic_bill_id",$this->getPropertyValue("gaic_bill_id"));
			$this->sql_model->where("gaic_goods_id",$this->getPropertyValue("gaic_goods_id"));
		}
		protected function update($data){
			$this->setWhere();

			$result =  $this->sql_model->update($data);
			if(!$result){
				$this->throwException("Update Value Failed!");
			}
			return $result;
		}
		function getAmountFromDb(){
			$this->setWhere();

			$result = $this->getOne("*");

			if($result){
				return $result["gaic_current_so_luong"];
			}
			else{
				$this->throwException("Get Amount Failed, No record!");
			}
		}
		function setAmountToDb($amount){
			if($amount){
				$this->setWhere();
				$result = $this->update(array("gaic_current_so_luong" => $amount));

				if(!$result){
					$this->throwException("Set Amount Failed !");		
				}
				return $result;
			}
			else{
				$this->throwException("Set Amount Failed, Amount == 0");		
			}
		}
		function increaseAmount($amount){
				$current_amount = $this->getAmountFromDb();
				$new_amount = $current_amount + $amount;
				if($new_amount < 0){
					$this->throwException("Increase Amount Failed, new Amount < 0!");
				}
				else{
					$result = $this->setAmountToDb($new_amount);
					if(!$result){
						$this->throwException("Increase Amount Failed!");
					}
					return $result;
				}
		}
		function decreaseAmount($amount){
			try{
				return $this->increaseAmount($amount * -1);
			}
			catch(Exception $e){
				$this->throwException("Decrease Amount Failed!");
			}
		}
		function add(){
			$gia_ban = $this->getPropertyValue($this->getEnglishName()."_gia_ban"));
			$so_luong = $this->getPropertyValue($this->getEnglishName()."_current_so_luong");
			$this->setPropertyValue($this->getEnglishName()."_tong_tien_ban") = $gia_ban * $so_luong;
			return $this->insert();
		}
	}
?>