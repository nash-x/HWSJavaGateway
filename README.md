# HWSJavaGateway
Because HWClouds(www.hwclouds.com) not support python API, it only support JAVA API.

Our code is writen in python, we need to call java API of HWClouds in python code.

That the reason I writen this HWSJavaGateway.

Our release *hws_gateway_v0.1.jar* is base on py4j, it is capsulated of rest method for hws java sdk.

We use it to call java SDK method of hwclouds.

## Introduction about HWClouds java SDK ##
http://support.hwclouds.com/api-ecs/zh-cn_topic_0020805992.html

## How to run hws_gateway ##
hws_gateway_v0.1.jar is an executable jar file for tag: hws_v0.1.

It can execute by following command:

**# java -jar hws_gateway_v0.1.jar**

## How to call API of hws_gateway in Python code ##
We can use REST method in Python to call HWClouds java SDK after running hws_gateway_v0.1.jar like this:

    from py4j.java_gateway import JavaGateway, GatewayParameters
    class HWSRestMethod(object):
    gateway = JavaGateway(gateway_parameters=GatewayParameters(port=25535))
    rest_method_java = gateway.entry_point

	@staticmethod
	def get(ak, sk, request_url, service_name, region):
        return HWSRestMethod.rest_method_java.get(ak, sk, request_url, service_name, region)

	@staticmethod
	def put(ak, sk, request_url, body, service_name, region):
    	return HWSRestMethod.rest_method_java.put(ak, sk, request_url, body, service_name, region)

	@staticmethod
	def post(ak, sk, request_url, body, service_name, region):
    	return HWSRestMethod.rest_method_java.post(ak, sk, request_url, body, service_name, region)

	@staticmethod
	def patch(ak, sk, request_url, body, service_name, region):
    	return HWSRestMethod.rest_method_java.patch(ak, sk, request_url, body, service_name, region)

	@staticmethod
	def delete(ak, sk, requestUrl, service_name, region):
    	return HWSRestMethod.rest_method_java.delete(ak, sk, requestUrl, service_name, region)
