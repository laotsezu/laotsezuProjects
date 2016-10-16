<?php 

	class FollowMe extends iBNC{
		function __construct(){
			global $_B; 
			$this->r = $_B['r'];   
			$this->cache = $_B['cache'];   
			$controller = $this->r->get_string('controller','GET');
			$this->action = $this->r->get_string('action','GET');
			
			$this->d = array(); 

			if(method_exists($this, $controller)){
				$this->$controller();
			}
			else{
				$this->index();
			}
		}
		function postlocation(){
			$userId = $this->r->get_string("userId","POST");
			$userLocation = $this->r->get_string("userLocation","POST");

			$this->check_empty($userId);
			$this->check_empty($userLocation);

			$user_data["fu_id"] = $userId;
			$user_data["fu_location"] = $userLocation;

			$fu_model = $this->model("ModelFollowmeUser",$user_data,"followme");

			try{
				if(!$fu_model->exist()){
					$fu_model->addUser();
				}

				$fu_model->postLocation();
				$response["status"] = true;
			}
			catch(Exception $e){
				$response["status"] = false;
				$response["message"] = $e->getMessage();
			}
			echo json_encode($response);
		}
	}

?>