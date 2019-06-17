package com.bridgeit.note.service;

import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgeit.note.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class ElasticService {
	
	private RestHighLevelClient client;

    private ObjectMapper objectMapper;

    @Autowired
    public ElasticService(RestHighLevelClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public String escreateNote(Note note) throws Exception {

       String INDEX="elastic";
       String TYPE="createNote";
    	
        Map<String, Object> documentMapper = objectMapper.convertValue(note, Map.class);

		IndexRequest indexRequest = new IndexRequest(/* INDEX,TYPE, */String.valueOf(note.getNoteId()))
                .source(documentMapper).index(INDEX).type(TYPE);

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

        return indexResponse.getResult().name();
    }

}
