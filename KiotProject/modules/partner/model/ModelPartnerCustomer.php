<?php 

	class ModelPartnerCustomer extends BaseModel{	
		static $TAG = "ModelPartnerCustomer: ";
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"customer_id"
			,"customer_ten"
			,"customer_sdt"
			,"customer_nhom"
			,"customer_loai"
			,"customer_gioi_tinh"
			,"customer_ngay_sinh"
			,"customer_email"
			,"customer_dia_chi"
			,"customer_no_nan"
			,"customer_tong_tien_mua"
			,"customer_ma_so_thue"
			,"customer_status"
			);
		}
		function getEnglishName(){
			return "customer";
		}
		function getTableName(){
			return "khach_hang";
		}
		function addCustomer(){
			try{
				$result = $this->insert();
				if(!$result){
					$this->throwException("add customer failed 1!");
				}
				return $result;
			}
			catch(Exception $e){
				$this->throwException("add customer failed 2!");
			}
		}
		function searchCustomer($q,$start=0,$limit=10){
			if(!$start){
				$start = 0;
			}
			if(!$limit){
				$limit = 20;
			}

			if($q){
				$this->sql_model->where("customer_id",$q,"LIKE");
				$this->sql_model->orWhere("customer_ten",$q,"LIKE");
			}

			$this->sql_model->orderBy("customer_id","DESC");
			$result = $this->sql_model->get(null,array($start,$limit),"*");

			return $result;
		}
		function increaseTotalBuy($amount){
			$total_buy_index = $this->getEnglishName()."_tong_tien_mua";
			$result = $this->increasePropertyValueToDb($total_buy_index,$amount);
			if(!$result){
				$this->throwException("Increase total buy failed!");				
			}
			return $result;
		}
		function decreaseTotalBuy($amount){			
			$total_buy_index = $this->getEnglishName()."_tong_tien_mua";
			$result = $this->decreasePropertyValueToDb($total_buy_index,$amount);
			if(!$result){
				$this->throwException("Decrease total buy failed!");				
			}
			return $result;
		}
		function increaseOwe($amount){
			if($amount){
				$owe_index = $this->getEnglishName()."_no_nan";
				$result =  $this->increasePropertyValueToDb($owe_index,$amount);
				if(!$result)
					$this->throwException("Increase Owe Failed!");
				return $result;
			}
		}
		function decreaseOwe($amount){
			if($amount){
				$owe_index = $this->getEnglishName()."_no_nan";
				$result =  $this->decreasePropertyValueToDb($owe_index,$amount);
				if(!$result)
					$this->throwException("Decrease Owe failed! ");
				return $result;
			}			
		}
	}

?>