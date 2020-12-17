package com.example.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.json.simple.JSONObject;

//import org.json.JSONException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@RestController
@Component
public class Consumer implements ChannelAwareMessageListener {
	RestTemplate restTemplate = new RestTemplate();
	ObjectMapper objectMapper = new ObjectMapper();

	// @ResponseBody
	@Override
	public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {

		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		byte[] body = message.getBody();
		String receive = new String(body);
		JSONObject testV = new JSONObject();
		testV = objectMapper.readValue(receive.toString(), JSONObject.class);
		System.out.println(testV);
		String url = "http://192.168.91.87:9191/server/store";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>(testV, headers);
			restTemplate.postForObject(url, entity, String.class);
			// String response = restTemplate.postForObject(url, entity, String.class);
			// System.out.println(answer);
		} catch (Exception e) {
		}
	}
}
