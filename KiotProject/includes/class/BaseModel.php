<?php 
	abstract class BaseModel extends iBNC{
		//Nguyên tắc chung của tất cả các class trong thư mục Model là luôn ném ra 1 ngoại lệ (kèm thông điệp) nếu quá trình thực thi xảy ra lỗi
		//Tất cả các function trong file main.php đều phải try, catch những ngoại lệ đó để cho ra các response tương ứng.
		var $data;
		var $data_indexes;
		var $sql_model;
		static $TAG = "BaseModel: ";
		function __construct($input){
			$this->data = $input;
			$this->sql_model = $this->getModel($this->getTableName());
			$this->setDataIndexes();
		}

		abstract function setDataIndexes();
		abstract function getEnglishName();
		abstract function getTableName();

		public function buildOutput($indexes){
			$output = array();
			if(!$indexes){
				$indexes = $this->data_indexes;
			}
			for($i = 0;$i < count($indexes);$i++){
				$index = $indexes[$i];
				$output[$index] = $this->data[$index];
			}
			return $output;
		}
		function getPropertyValue($property){
			return $this->data[$property];
		}
		function getPropertyValueWithoutEmptyResult($property){
			if($this->data[$property])
				return $this->data[$property];
			else
				$this->throwException("property value is empty");
		}
		function setPropertyValue($property,$value){
			$this->data[$property] = $value;
		}
		function getPropertyValueFromDb($property){
			$id_index = $this->getEnglishName()."_id";
			$this->sql_model->where($id_index,$this->getPropertyValue($id_index));
			$result = $this->sql_model->getOne(null,"*");
			if(!$result)
				return null;
			else{
				return $result[$property];
			}
		}
		function setPropertyValueToDb($property,$value){
			$id_index = $this->getEnglishName()."_id";
			$this->sql_model->where($id_index,$this->getPropertyValue($id_index));
			$result = $this->sql_model->update(array($property => $value));	

			if(!$result){
				$this->throwException("Set Property Value To Db Failed!");
			}

			return $result;
		}
		function increasePropertyValueToDb($property,$value){
			if(!is_numeric($value))
				return false;

			$old_value = $this->getPropertyValueFromDb($property);
			$new_value = $old_value + $value;

			return 
				$this->setPropertyValueToDb($property,$new_value);
			
		}
		function decreasePropertyValueToDb($property,$value){
			return $this->increasePropertyValueToDb($property,$value * -1);
		}
		function decreasePropertyValueToDbWithoutNegative($property,$value){
			if(!is_numeric($value))
				return false;

			$old_value = $this->getPropertyValueFromDb($property);
			$new_value = $old_value - $value;

			if($new_value >= 0){
				return $this->setPropertyValueToDb($property,$new_value);
			}
			else{
				$this->throwException("Failed, new value < 0");
			}
		}
		function hasProperty($property){
			for($i = 0;$i < count($this->data_indexes) ; $i++){
				$index = $this->data_indexes[$i];
				if($index == $property){
					return true;
				}
			}
			return false;
		}
		function getDataIndexes(){
			return $this->data_indexes;
		}
		function insert(){
			if(!$this->data){
				$this->err_message(self::$TAG."Insert Failed!, Data == null ");
			}

			$time_index = $this->getEnglishName()."_thoi_gian";
			$last_modified_index = $this->getEnglishName()."_last_modified";
			$status_index = $this->getEnglishName()."_status";
			$current_time = $this->getCurrentTime();

			if($this->hasProperty($time_index))
				$this->setPropertyValue($time_index,$current_time);

			if($this->hasProperty($last_modified_index))
				$this->setPropertyValue($last_modified_index,$current_time);

			$this->setPropertyValue($status_index,1);
			
			$result = $this->sql_model->insert($this->data);

			if(!$result)
				throw new Exception(self::$TAG."Insert Failed !", 1);
				
		}

		protected function update($data){
			$id_index = $this->getEnglishName()."_id";
			$last_modified_index = $this->getEnglishName()."_last_modified";

			if($this->hasProperty($last_modified_index)){
				$this->setPropertyValue($last_modified_index,$this->getCurrentTime());
			}

			$this->sql_model->where($id_index,$this->getPropertyValue($id_index));

			$result = $this->sql_model->update($data);

			if(!$result)
				throw new Exception(self::$TAG."Update Failed !", 1);
		}
		
		function getOne($columns='*'){
			$id_index = $this->getEnglishName()."_id";
			$this->sql_model->where($id_index,$this->getPropertyValue($id_index));
			return $this->sql_model->getOne(null,$columns);
		}
		function get($start=0,$limit=10,$columns='*'){
			$this->sql_model->orderBy($this->getEnglishName()."_id","DESC");
			return $this->sql_model->get(null,array($start,$limit),$columns);
		}
		function getCurrentTime(){
			return @time();
		}
		function explodeGoodsIds($goods_ids){
			$items = array();
			$goodses = explode(",",$goods_ids);
			for($i = 0; $i < count($goodses); $i++){
				$goods = $goodses[$i];
				$goods = explode("=",$goods);
				$goods_id = $goods[0];
				$goods_count = $goods[1];
				$item = array("goods_id" => $goods_id,"goods_so_luong" => $goods_count);
				$items[] = $item;
			}
			return $items;
		}
		function setData($data){
			$this->data = $data;
		}
		function getData(){
			return $this->data;
		}
		function getLastInsertId(){
			$this->sql_model->orderBy($this->getEnglishName()."_id","DESC");
			$result = $this->sql_model->getOne(null);
			if(!$result)
				throw new Exception("Get Last Insert Id Of ".$this->getEnglishName()." == null", 1);
			return $result[$this->getEnglishName()."_id"];
		}
		function err_message($message){
			$response["status"] = false;
			$response["message"] = $message;

			die(json_encode($response));
		}
		function unSetData($property){
			unset($this->data[$property]);
		}
		function throwException($message){
			throw new Exception(self::$TAG.$message, 1);			
		}
	}
?>
