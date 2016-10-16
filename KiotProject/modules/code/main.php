<?php
class Code extends iBNC{
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
	function n(){
		
	}
	function m(){
		
	}
	function l(){
	
	}
}
?>