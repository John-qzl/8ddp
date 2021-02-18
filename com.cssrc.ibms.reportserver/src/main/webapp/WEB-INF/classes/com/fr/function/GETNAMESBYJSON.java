package com.fr.function;

import com.fr.script.AbstractFunction;
import java.io.IOException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetNamesByJSON extends AbstractFunction
{
  public Object run(Object[] args)
  {
    String name = "";
    try
    {
      JSONArray parasArray = JSONArray.fromObject(args[0].toString());

      if (parasArray.isEmpty()) {
        return "参数为空";
      }
      JSONObject jsonobject = new JSONObject();
      if (!parasArray.isEmpty()) {
        for (int i = 0; i < parasArray.size(); i++) {
          jsonobject = parasArray.getJSONObject(i);
          name = name + (String)jsonobject.get("name") + " ";
        }

        return name;
      }
    } catch (Exception e) {
      return "error";
    }
    return "空";
  }
  public static void main(String[] arg) throws IOException {
    String[] ss = new String[1];
    ss[0] = "[{\"id\":\"1302\",\"name\":\"2016-4-8.docx\"},{\"id\":\"1350\",\"name\":\"zhgl20160317.dmp\"}]";

    GetNamesByJSON tt = new GetNamesByJSON();
    Object aa = tt.run(ss);
  }
}