<?php 
	class ModelPartnerProvider extends BaseModel{
		static $TAG = "ModelPartnerProvider: ";
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"provider_id"
			,"provider_ten"
			,"provider_sdt"
			,"provider_email"
			,"provider_dia_chi"
			,"provider_cong_ty"
			,"provider_no_nan"
			,"provider_tong_tien_mua"
			,"provider_ma_so_thue"
			,"provider_status"
			);
		}
		function getEnglishName(){
			return "provider";
		}
		function getTableName(){
			return "nha_cung_cap";
		}
		function searchProvider($q,$start=0,$limit=10){
			if(!$start){
				$start = 0;
			}
			if(!$limit){
				$limit = 20;
			}

			if($q){
				$this->sql_model->where("provider_id",$q,"LIKE");
				$this->sql_model->orWhere("provider_ten",$q,"LIKE");
			}

			$this->sql_model->orderBy("provider_id","DESC");
			$result = $this->sql_model->get(null,array($start,$limit),"*");

			return $result;
		}
		function addProvider(){
			try{
				$result = $this->insert();
				if(!$result){
					$this->throwException("add provider failed 1!");
				}
				return $result;
			}
			catch(Exception $e){
				$this->throwException("add provider failed 2")
			}
			return $response;
		}
		function increaseTotalBuy($amount){
			
			$total_buy_index = $this->getEnglishName()."_tong_tien_mua";
			$result = $this->increasePropertyValueToDb($total_buy_index,$amount);
			if(!$result){
				$this->throwException("Increase total buy failed!");				
			}
			return $result;
		}
		function increaseOwe($amount){
			if($amount){
				$owe_index = $this->getEnglishName()."_no_nan";
				$result =  $this->increasePropertyValueToDb($owe_index,$amount);
				if(!$result)
					$this->throwException("Increase Owe Failed! ");
				return $result;
			}
		}
		function decreaseOwe($amount){
			if($amount){
				$owe_index = $this->getEnglishName()."_no_nan";
				$result =  $this->decreasePropertyValueToDb($owe_index,$amount);

				if(!$result){
					$this->throwException("Decrease Owe failed! ");
				}
				return $result;
			}
		}
	}
	



?>