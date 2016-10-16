<?php 
	class ModelRepositoryGoods extends BaseModel{
		static $TAG = "ModelRepositoryGoods: ";
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"goods_id"
			,"goods_ten"
			,"goods_nhom"
			,"goods_loai"
			,"goods_gia_ban"
			,"goods_gia_von"
			,"goods_giam_gia"
			,"goods_so_luong"
			,"goods_don_vi"
			,"goods_icon"
			,"goods_status"
			);
		}
		function getEnglishName(){
			return "goods";
		}
		function getTableName(){
			return "hang_hoa";
		}
		function searchGoods($keyword,$start = 0,$limit = 10){
			if($keyword){
				$this->sql_model->where("goods_id",$keyword,"LIKE");
				$this->sql_model->orWhere("goods_ten",$keyword,"LIKE");
			}
			$this->sql_model->orderBy($this->getEnglishName()."_id");
			$result = $this->sql_model->get(null,array($start,$limit),"*");

			return $result;
		}
		function insertNewGoods(){
			try{
				if(!$this->getPropertyValue("goods_gia_von")){
					$this->setPropertyValue("goods_gia_von",$this->getPropertyValue("goods_gia_ban"));
				}

				$result = $this->insert();
				
				return $result;
			}
			catch(Exception $e){
				$this->throwException("Insert new Goods failed");
			}
		}
		function setAmount($amount){
			$amount_index = $this->getEnglishName()."_so_luong";
			if(!$amount){
				$amount = $this->getPropertyValue($amount_index);
			}
			
			if($amount < 0){
				$this->throwException("Failed, goods amount unable < 0");
			}

			$result =  $this->setPropertyValueToDb($amount_index,$amount);

			if(!$result)
				$this->throwException("Set Amount failed!");

			return $result;
		}
		function increaseAmount($amount){
			$amount_index = $this->getEnglishName()."_so_luong";
			if(!$amount){
				$amount = $this->getPropertyValue($amount_index);
			}
			if($amount){
				$result =  $this->increasePropertyValue($amount_index,$amount);
				if(!$result){
					$this->throwException("Increase Amount failed!");
					return $result;
				}
			}
		}
		function decreaseAmount($amount){ 
			$amount_index = $this->getEnglishName()."_so_luong";
			if(!$amount){
				$amount = $this->getPropertyValue($amount_index);
			}
			if($amount){
				$result =  $this->decreasePropertyValueToDbWithoutNegative($amount_index,$amount);
				if(!$result){
					$this->throwException("Decrease Amount failed!");
				}
				return $result;
			}
		}
	}
?>