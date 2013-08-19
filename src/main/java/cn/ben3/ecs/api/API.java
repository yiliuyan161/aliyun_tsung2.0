package cn.ben3.ecs.api;

import cn.ben3.ecs.client.logic.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class API
{
    private static final String ACCESS_KEY_ID = Context.prop("ACCESS_KEY_ID");     // 请在这里填写你的Access Key ID
    private static final String ACCESS_KEY_SECRET = Context.prop("ACCESS_KEY_SECRET");// 请在这里填写你的Access Key Secret
    private static List<String> regions=new ArrayList<String>();
    private static Map<String, List<String>> zones = new HashMap<String, List<String>>();

    public static void main( String[] args ) throws Exception {

//           JSONObject reg= listRegions();
//            JSONArray regionArray= reg.getJSONObject("Regions").getJSONArray("Region");
//            for (int i = 0; i < regionArray.size(); i++) {
//                JSONObject region = regionArray.getJSONObject(i);
//                regions.add(region.getString("RegionId"));
//
//            }
//            for (String regionId : regions) {
//               JSONObject zo= listZones(regionId);
//                JSONArray zoneArray= zo.getJSONObject("Zones").getJSONArray("Zone");
//                List<String> zoneIds=new ArrayList<String>();
//                for (int i = 0; i < zoneArray.size(); i++) {
//                    JSONObject zone = zoneArray.getJSONObject(i);
//                    zoneIds.add(zone.getString("ZoneId"));
//                }
//                zones.put(regionId, zoneIds);
//            }
//            for (String regionId : zones.keySet()) {
//                List<String> zoneList = zones.get(regionId);
//                for (String zoneId : zoneList) {
//                    describeInstanceStatus(regionId, zoneId);
//                }
//            }
            describeInstanceAttribute("AY130814080510052b52");
//            modifyInstanceAttribute("AY130814080510052b52",new HashMap<String, String>(){
//                {put("Password", "tsk980");}
//            });
            rebootInstance("AY130814080510052b52", true);






    }

    public static JSONObject describeRegions() throws Exception {
        String resp=newRequest().execute("DescribeRegions", null);
        JSONObject json = JSON.parseObject(resp);
        
        return json;
    }

    @SuppressWarnings("serial")
    public static JSONObject describeZones(final String regionId) throws Exception {
        String resp = newRequest().execute("DescribeZones",
                new HashMap<String, String>() {
                    { put("RegionId", regionId); }
                });
        JSONObject json = JSON.parseObject(resp);
        
        return json;
    }

    @SuppressWarnings("serial")
    public static JSONObject listImages(final String regionId) throws Exception {
        // 从第2页起读取20条Image信息
        String resp = newRequest().execute("DescribeImages",
                new HashMap<String, String>() {
                    { put("RegionId", regionId); }
                    { put("PageNumber", "2"); }
                    { put("RageSize", "20"); }
                });

        
        JSONObject json = JSON.parseObject(resp);
        return json;
    }

    @SuppressWarnings("serial")
    public static JSONObject describeInstanceStatus(final String regionId, final String zoneId) throws Exception {
        String resp = newRequest().execute("DescribeInstanceStatus",
                new HashMap<String, String>() {
                    { put("RegionId", regionId); }
                    { put("ZoneId", zoneId); }
                });
        JSONObject json = JSON.parseObject(resp);
        
        return json;
    }
    @SuppressWarnings("serial")
    public static JSONObject startInstance(final String instanceId)
            throws Exception {
        String response =
                newRequest().execute("StartInstance", new HashMap<String, String>() {
                    { put("InstanceId", instanceId); }
        });
        
        JSONObject json = JSON.parseObject(response);
        return json;
    }
    @SuppressWarnings("serial")
    public static JSONObject stopInstance(final String instanceId,final boolean force)
            throws Exception {
        String response =
                newRequest().execute("StopInstance", new HashMap<String, String>() {
                    { put("InstanceId", instanceId); }
                    { put("ForceStop", force?"true":"false"); }
        });
        
        JSONObject json = JSON.parseObject(response);
        return json;
    }
    @SuppressWarnings("serial")
    public static JSONObject rebootInstance(final String instanceId,final boolean force)
            throws Exception {
        String response =
                newRequest().execute("RebootInstance", new HashMap<String, String>() {
                    { put("InstanceId", instanceId); }
                    { put("ForceStop", force?"true":"false"); }
        });
        
        JSONObject json = JSON.parseObject(response);
        return json;
    }

    @SuppressWarnings("serial")
    public static JSONObject resetInstance(final String instanceId,final String imageId,final String diskType)
            throws Exception {
        String response =
                newRequest().execute("ResetInstance", new HashMap<String, String>() {
                    { put("InstanceId", instanceId); }
                    { put("ImageId", imageId); }
                    { put("DiskType", diskType); }
        });
        
        JSONObject json = JSON.parseObject(response);
        return json;
    }

    @SuppressWarnings("serial")
    public static JSONObject modifyInstanceAttribute(final String instanceId, Map<String,String> param)
            throws Exception {
        param.put("InstanceId", instanceId);

        String response =
                newRequest().execute("ModifyInstanceAttribute", param);
        
        JSONObject json = JSON.parseObject(response);
        return json;
    }
    @SuppressWarnings("serial")
    public static JSONObject describeInstanceStatus(final String regionId,final String zoneId,final String pageNumber,final String pageSize)
            throws Exception {
        String response =
                newRequest().execute("DescribeInstanceStatus", new HashMap<String, String>() {
                    { put("RegionId", regionId); }
                    { put("ZoneId", zoneId); }
                    { put("PageNumber", pageNumber); }
                    { put("PageSize", pageSize); }
        });
        
        JSONObject json = JSON.parseObject(response);
        return json;
    }

    @SuppressWarnings("serial")
    public static JSONObject describeInstanceAttribute(final String instanceId)
            throws Exception {
        String response =
                newRequest().execute("DescribeInstanceAttribute", new HashMap<String, String>() {
                    { put("InstanceId", instanceId); }
                });
        
        JSONObject json = JSON.parseObject(response);
        return json;
    }


    public static EcsRequest newRequest() {
        return new EcsRequest(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }
}
