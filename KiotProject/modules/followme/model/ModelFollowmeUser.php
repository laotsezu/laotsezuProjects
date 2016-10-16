<?php 
	class ModelFollowmeUser extends BaseModel{
		static $TAG = "ModelFollowmeUser: ";
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"fu_id"
			,"fu_followme"
			,"fu_mefollow"
			,"fu_blocking"
			,"fu_location"
			,"fu_location_lasted_update"
			);
		}
		function getEnglishName(){
			return "fu";
		}
		function getTableName(){
			return "followme_user";
		}
		function postLocation(){
			$this->sql_model->where("fu_id",$this->getPropertyValueWithoutEmptyResult("fu_id"));
			$result = $this->sql_model->update(
				array("fu_location" => $this->getPropertyValueWithoutEmptyResult("fu_location")
						,"fu_location_lasted_update" => @time()
					));
			if($result)
				return true;
			else{
				$this->throwException("Post location failed!");
			}
		}
		function exist(){
			$this->sql_model->where("fu_id",$this->getPropertyValueWithoutEmptyResult("fu_id"));
			$result = $this->sql_model->getOne(null);
			if($result)
				return true;
			else
				return false;
		}
		function addUser(){
			$result = $this->sql_model->insert($this->getData());
			if($result)
				return true;
			else{
				$this->throwException("add user failed, insert Failed!");
			}
		}
		function getUser($userId=null){
			if(!$userId)
				$userId = $this->getPropertyValueWithoutEmptyResult("fu_id");

			$this->sql_model->where($userId);
			$user = $this->sql_model->getOne(null);

			if($user)
				return $user;
			else
				$this->throwException("Get user failed!, get one result is empty!");
		}
		function addFollowme($_otherUserId=null){	
			if(!$_otherUserId)
				$this->throwException("Add follow me failed, missing other user id!");

			$user = $this->getOne();

			if($user){
				$current_follow_me = $user["fu_followme"];

				if($current_follow_me){
					$new_follow_me = $current_follow_me . "," . $_otherUserId;
				}
				else{
					$new_follow_me = $_otherUserId;
				}

				$this->sql_model->where("fu_id",$this->getPropertyValueWithoutEmptyResult("fu_id"));
				$result = $this->sql_model->update(array("fu_followme" => $new_follow_me));

				if(!$result){
					$this->throwException("Add follow me failed, update new follow me failed!");
				}
				else{
					return $result;
				}
			}
			else{
				$this->throwException("Add follow me failed, user is not exist or missing id!");
			}

			
		}
		function addMeFollow($_otherUserId=null){
			if(!$_otherUserId)
				$this->throwException("Add follow me failed, missing other user id!");

			$user = $this->getOne();

			if($user){
				$current_me_follow = $user["fu_mefollow"];

				if($current_me_follow){
					$new_me_follow = $current_me_follow . "," . $_otherUserId;
				}
				else{
					$new_me_follow = $_otherUserId;
				}


				$this->sql_model->where("fu_id",$this->getPropertyValueWithoutEmptyResult("fu_id"));
				$result = $this->sql_model->update(array("fu_mefollow" => $new_me_follow));

				if(!$result){
					$this->throwException("Add me follow failed, update new  me follow failed!");
				}
				else{
					return $result;
				}
			}
			else{
				$this->throwException("Add me follow  failed, user is not exist or missing id!");
			}
		}

		function removeMefollow($_otherUserId=null){
			if(!$_otherUserId)
				$this->throwException("Remove me follow failed, missing other user id!");

			$user = $this->getOne();

			if($user){
				$current_me_follow = $user["fu_mefollow"];

				$currentUserIds = $this->explodeUserIds($current_me_follow);
				$newUserIds = array();

				for($i = 0; $i < count($currentUserIds); $i++){
					$userId = $currentUserIds[$i];
					if(!strcmp($userId,$_otherUserId)){
						$newUserIds[] = $userId;
					}
				}

				$this->sql_model->where("fu_id",$this->getPropertyValueWithoutEmptyResult("fu_id"));
				$result = $this->sql_model->update(array("fu_mefollow" => implode(",",$newUserIds)));

				if(!$result){
					$this->throwException("Remove me follow failed, update me follow failed!");
				}
				else{
					return $result;
				}

			}
			else{
				$this->throwException("Remove me follow failed, user is not exist or missing id!");
			}

		}
		function blockingFollowme($_otherUserId=null){
			if(!$_otherUserId)
				$this->throwException("Blocking follow me failed, missing other user id!");

			$user = $this->getOne();

			if($user){
				$current_blocking_followme = $user["fu_blocking"];
				if($current_blocking_followme){
					$new_blocking_followme = $current_blocking_followme . "," . $_otherUserId;
				}
				else{
					$new_blocking_followme = $_otherUserId;
				}

				$this->sql_model->where("fu_id",$this->getPropertyValueWithoutEmptyResult("fu_id"));
				$result = $this->sql_model->update(array("fu_blocking" => $new_blocking_followme));

				if(!$result)
					$this->throwException("Blocking follow me failed, update blocking failed!");
				else{
					return $result;
				}
			}	
			else{
				$this->throwException("Blocking follow me failed, user is not exist or missing id!");
			}

		}
		function unBlockingFollowme($_otherUserId=null){
			if(!$_otherUserId)
				$this->throwException("Unblocking follow me failed, missing other user id!");

			$user = $this->getOne();

			if($user){
				$current_blocking_followme = $user["fu_blocking"];

				$currentUserIds = $this->explodeUserIds($current_me_follow);
				$newUserIds = array();

				for($i = 0; $i < count($currentUserIds); $i++){
					$userId = $currentUserIds[$i];
					if(!strcmp($userId,$_otherUserId)){
						$newUserIds[] = $userId;
					}
				}

				$this->sql_model->where("fu_id",$this->getPropertyValueWithoutEmptyResult("fu_id"));
				$result = $this->sql_model->update(array("fu_blocking" => implode(",",$newUserIds)));

				if(!$result){
					$this->throwException("Unblocking follow me, update me follow failed!");
				}
				else{
					return $result;
				}

			}
			else{
				$this->throwException("Unblocking follow me failed, user is not exist or missing id!");
			}
		}
		function explodeUserIds($ids){
			if(!$ids)
				return $ids;
			else{
				return explode(",",$ids);
			}
		}
	}

?>