<?php 
class Partner extends iBNC{
		public function __construct(){ 
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

		function index(){ 			
			echo "123";
		}
		function timkhachhang(){
			$start = $this->r->get_int("start","GET",0);
			$limit = $this->r->get_int("limit","GET",10);
			$q = $this->r->get_int("q","GET");


			$customer = $this->model("ModelPartnerCustomer",null,"partner");

			try{
				$result = $customer->searchCustomer($q,$start,$limit);
				$response["items"] = $result;
				$response["status"] = true;
				$response["count"] = count($result);
			}
			catch(Exception $e){
				$response["status"] = false;
				$response["message"] = $e->getMessage();
			}

			echo json_encode($response);
		}
		function timnhacungcap(){
			$start = $this->r->get_int("start","GET",0);
			$limit = $this->r->get_int("limit","GET",10);
			$q = $this->r->get_int("q","GET");

			$provider = $this->model("ModelPartnerProvider",null,"partner");
			
			try{
				$result = $provider->searchProvider($q,$start,$limit);
				$response["items"] = $result;
				$response["status"] = true;
				$response["count"] = count($result);
			}
			catch(Exception $e){
				$response["status"] = false;
				$response["message"] = $e->getMessage();
			}

			echo json_encode($response);
		}
		function themkhachhang(){

			$khach_hang_input["customer_ten"] = $this->r->get_string("customer_ten","POST");
			$khach_hang_input["customer_sdt"] = $this->r->get_string("customer_sdt","POST");
			$khach_hang_input["customer_nhom"] = $this->r->get_string("customer_nhom","POST");
			$khach_hang_input["customer_loai"] = $this->r->get_string("customer_loai","POST");
			$khach_hang_input["customer_gioi_tinh"] = $this->r->get_string("customer_gioi_tinh","POST");
			$khach_hang_input["customer_ngay_sinh"] = $this->r->get_string("customer_ngay_sinh","POST");
			$khach_hang_input["customer_email"] = $this->r->get_string("customer_email","POST");
			$khach_hang_input["customer_dia_chi"] = $this->r->get_string("customer_dia_chi","POST");
			$khach_hang_input["customer_no_nan"] = $this->r->get_int("customer_no_nan","POST");
			$khach_hang_input["customer_tong_tien_mua"] = $this->r->get_int("customer_tong_tien_mua","POST");
			$khach_hang_input["customer_ma_so_thue"] = $this->r->get_string("customer_ma_so_thue","POST");
			
			$this->check_empty($khach_hang_input["customer_ten"]);

			$customer = $this->model("ModelPartnerCustomer",$khach_hang_input,"partner");

			try{
				$result = $customer->addCustomer();
				$response["status"] = true;
			}
			catch(Exception $e){	
				$response["status"] = false;
				$response["message"] = $e->getMessage();
			}

			echo json_encode($response);
		}
		function themnhacungcap(){

			$nhacc_input["provider_ten"] = $this->r->get_string("provider_ten","POST");
			$nhacc_input["provider_sdt"] = $this->r->get_string("provider_sdt","POST");
			$nhacc_input["provider_email"] = $this->r->get_string("provider_email","POST");
			$nhacc_input["provider_dia_chi"] = $this->r->get_string("provider_dia_chi","POST");
			$nhacc_input["provider_cong_ty"] = $this->r->get_string("provider_cong_ty","POST");
			$nhacc_input["provider_no_nan"] = $this->r->get_string("provider_no_nan","POST");
			$nhacc_input["provider_ma_so_thue"] = $this->r->get_string("provider_ma_so_thue","POST");

			$this->check_empty($khach_hang_input["provider_ten"]);
			$provider = $this->model("ModelPartnerProvider",$nhacc_input,"partner");

			try{
				$result = $provider->addProvider();
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