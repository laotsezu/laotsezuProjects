<?php 
class Repository extends iBNC{
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
		function timkiemhang(){
			$keyword = $this->r->get_string("q","GET");
			$start = $this->r->get_int("start","GET");
			$limit = $this->r->get_int("limit","GET");

			if(!$start)$start = 0;
			if(!$limit)$limit = 10;

			$goods = $this->model("ModelRepositoryGoods",null,"repository");

			try{
				$response["items"] = $goods->searchGoods($keyword,$start,$limit);
				$response["count"] = count($response["items"]);
				$response["status"] = true;
			}
			catch(Exception $e){
				$response["status"] = false;
				$response["message"] = $e->getMessage();
			}

			echo json_encode($response);
		}
		function themhang(){

			$input["goods_ten"] = $this->r->get_string("goods_ten","POST");
			$input["goods_nhom"] = $this->r->get_string("goods_nhom","POST");
			$input["goods_loai"] = $this->r->get_string("goods_loai","POST");
			$input["goods_gia_ban"] = $this->r->get_int("goods_gia_ban","POST");
			$input["goods_gia_von"] = $this->r->get_int("goods_gia_von","POST");
			$input["goods_so_luong"] = $this->r->get_int("goods_so_luong","POST");
			$input["goods_icon"] = $this->r->get_int("goods_icon","POST");
			$input["goods_don_vi"] = $this->r->get_int("goods_don_vi","POST");

			$this->check_emptys(array($input["goods_ten"]));
			$this->check_moneys(array($input["goods_gia_ban"]));

			$input["goods_status"] = $this->r->get_int("status","POST",1);

			$goods = $this->model("ModelRepositoryGoods",$input,"repository");

			try{
				$response["status"] = true;
			}
			catch(Exception $e){
				$response["status"] = false;
				$response["message"] = $e->getMessage();
			}

			echo json_encode($response);

		}
		//Kiểm hàng chứ không phải là kiếm hàng
		function kiemhang(){
			$phieu_kiem_hang["agency_id"] = $this->r->get_int("agency_id","POST");
			$phieu_kiem_hang["personnel_id"] = $this->r->get_int("personnel_id","POST");
			$phieu_kiem_hang["origin_goods_ids"] = $this->r->get_string("origin_goods_ids","POST");
			$phieu_kiem_hang["current_goods_ids"] = $this->r->get_string("current_goods_ids","POST");
			$phieu_kiem_hang["coupon_ghi_chu"] = $this->r->get_string("coupon_ghi_chu","POST");

			$this->check_emptys(
				array($phieu_kiem_hang["origin_goods_ids"]
					,$phieu_kiem_hang["current_goods_ids"])
				);
			
			$coupon = $this->model("ModelRepositoryCoupon",$phieu_kiem_hang,"repository");

			try{
				$result = $coupon->accept();
				$response["status"] = true;
			}
			catch(Exception $e){
				$response["status"] = false;
				$response["message"] = $e->getMessage();
			}

			echo json_encode($response);
		}
		
		function nhaphang(){
			$phieu_nhap_hang["provider_id"] = $this->r->get_int("provider_id","POST");
			$phieu_nhap_hang["agency_id"] = $this->r->get_int("agency_id","POST");
			$phieu_nhap_hang["personnel_id"] = $this->r->get_int("personnel_id","POST");
			$phieu_nhap_hang["icoupon_tong_tien_hang"] = $this->r->get_int("icoupon_tong_tien_hang","POST");
			$phieu_nhap_hang["icoupon_giam_gia"] = $this->r->get_int("icoupon_giam_gia","POST");
			$phieu_nhap_hang["icoupon_tien_phai_tra"] = $this->r->get_int("icoupon_tien_phai_tra","POST");
			$phieu_nhap_hang["icoupon_tien_da_tra"] = $this->r->get_int("icoupon_tien_da_tra","POST");
			$phieu_nhap_hang["icoupon_ghi_chu"] = $this->r->get_string("icoupon_ghi_chu","POST");
			$phieu_nhap_hang["icoupon_goods_ids"] = $this->r->get_string("icoupon_goods_ids","POST");

			$this->check_emptys(
				array(
					$phieu_nhap_hang["icoupon_goods_ids"]
				)
			);

			$this->check_moneys(
				array(
					$phieu_nhap_hang["icoupon_tong_tien_hang"]
					,$phieu_nhap_hang["icoupon_tien_phai_tra"] 
					,$phieu_nhap_hang["icoupon_tien_da_tra"] 
				)
			);

			$icoupon = $this->model("ModelRepositoryICoupon",$phieu_nhap_hang,"repository");

			try{
				$result = $icoupon->accept();
				$response["status"]= true;
			}
			catch(Exception $e){
				$response["status"] = false;
				$response["message"] = $e->getMessage();
			}

			echo json_encode($response);
		}
		function duyetkho(){
			$start = $this->r->get_int("start","GET");
			$limit = $this->r->get_int("limit","GET");

			if(!$start)$start = 0;
			if(!$limit)$limit = 10;

			$goods = $this->model("ModelRepositoryGoods",null,"repository");
			$items = $goods->get($start,$limit);

			if(!$items){
				$response["status"] = false;
				$response["message"] = "Main Repository: " . "Get Goodses Failed!";
			}
			else{
				$response["status"] = true;
				$response["items"] = $items;
				$response["count"] = count($items);
			}

			echo json_encode($response);
		}
	}

?>