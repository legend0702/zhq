var script = {

};

validate = function(data, config) {
	data = data || {};
	config = config || {};
	var result = {
		"msg" : ""
	};
	var name = data['name'];
	if (!name) {
		result["msg"] = config['name'] ? config['name'] : "" + "不能为空!";
	}
	return result;
};