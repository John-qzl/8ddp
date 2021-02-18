Namespace.register("com.ibms.form.rule");  
com.ibms.form.rule.CustomRules=[<#list	ruleList as rule>
	{
		name:"${rule.name}",
		rule:function(v){
				return /${rule.rule}/.test(v);
		},
		msg:"${rule.tipInfo}"
	}<#if rule_has_next>,</#if>
</#list>];
