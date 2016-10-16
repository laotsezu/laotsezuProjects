<?php 
	class ModelFundFund extends BaseModel{
		static $TAG ="ModelFundFund: ";
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"fund_id"
			,"agency_id"
			,"personnel_id"
			,"fund_tong_quy"
			,"fund_tong_thu"
			,"fund_tong_chi"
			,"fund_last_mofified"
			,"fund_status"
			);
		}
		function getEnglishName(){
			return "fund";
		}
		function getTableName(){
			return "quy";
		}
		function setWhere(){
			$this->sql_model->where("agency_id",$this->getPropertyValue("agency_id"));
		}
		function checkChangeAvailable(){
			$result = $this->sql_model->getOne(null);
			if(!$result){
				$this->throwException("Agency was not have Fund yet!");
			}
			else{
				$personnel_id_avaiable = $result["personnel_id"];
				if($personnel_id_avaiable !== $this->getPropertyValue("personnel_id")){
					$this->throwException("Personnel unable change Fund Amount !");
				}
			}
		}
		function getOneFund(){
			$this->setWhere();
			$result = $this->sql_model->getOne(null);
			if($result){
				return $result;
			}
			else{
				$this->throwException("Fund of agency is not exist!");			
			}
		}

		function getFundAmount(){
			$one_fund = $this->getOneFund();
			return $one_fund["fund_tong_quy"];
		}
		function getThuAmount(){
			$one_fund = $this->getOneFund();
			return $one_fund["fund_tong_thu"];
		}
		function getChiAmount(){
			$one_fund = $this->getOneFund();
			return $one_fund["fund_tong_chi"];
		}
		function setFundAmounts($tong,$thu,$chi){
			$this->setWhere();
			$this->checkChangeAvailable();

			$data = array();

			if($tong){
				$data["fund_tong_quy"] = $tong;
			}
			if($thu){
				$data["fund_tong_thu"] = $thu;
			}
			if($chi){
				$data["fund_tong_chi"] = $chi;
			}	

			if($data){
				$data["fund_last_modified"] = $this->getCurrentTime();

				$result = $this->sql_model->update($data);

				if(!$result){
					$this->throwException("Set Fund Amounts Failed !");					
				}
				return $result;
			}
			else{
				$this->throwException("Set Fund Amounts Failed, Data update == null ");				
			}
		}
		function decreaseFund($amount){
			if($amount > 0){
				$current_tong = $this->getFundAmount();
				$current_tong -= $amount;

				$current_chi = $this->getChiAmount();
				$current_chi += abs($amount);

				$this->setFundAmounts($current_tong,null,$current_chi);
			}
			else{
				$this->throwException("Decrease Fund Failed , Amount <= 0 ");
			}
		}
		function increaseFund($amount){
			if($amount > 0){
				$current_tong = $this->getFundAmount();
				$current_tong += $amount;
				
				$current_thu = $this->getThuAmount();
				$current_thu += $amount;

				$this->setFundAmounts($current_tong,$current_thu,null);
			}	
			else{
				$this->throwException("Increase Fund Failed , Amount <= 0 ");
			}
		}
	}
?>