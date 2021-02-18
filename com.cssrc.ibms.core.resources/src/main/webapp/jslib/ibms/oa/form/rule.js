Namespace.register("com.ibms.form.rule");  
com.ibms.form.rule.CustomRules=[
	{
		name:"非负整数",
		rule:function(v){
				return /^\d+$/.test(v);
		},
		msg:"必须为非负整数"
	}
];
