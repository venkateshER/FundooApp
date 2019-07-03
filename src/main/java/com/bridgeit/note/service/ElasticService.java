//package com.bridgeit.note.service;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.lucene.search.join.ScoreMode;
//import org.elasticsearch.action.delete.DeleteRequest;
//import org.elasticsearch.action.delete.DeleteResponse;
//import org.elasticsearch.action.get.GetRequest;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.update.UpdateRequest;
//import org.elasticsearch.action.update.UpdateResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.bridgeit.note.model.Note;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Service
//public class ElasticService {
//
//	private RestHighLevelClient client;
//
//	private ObjectMapper objectMapper;
//
//	@Autowired
//	public ElasticService(RestHighLevelClient client, ObjectMapper objectMapper) {
//		this.client = client;
//		this.objectMapper = objectMapper;
//	}
//
//	String INDEX = "elasticsearch";
//	String TYPE = "searchNotes";
//
//	public String escreateNote(Note note) throws Exception {
//
//		Map<String, Object> documentMapper = objectMapper.convertValue(note, Map.class);
//
//		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, String.valueOf(note.getNoteId()))
//				.source(documentMapper);// .index(INDEX).type(TYPE);
//
//		IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
//
//		return indexResponse.getResult().name();
//	}
//
//	public Note findById(String noteId) throws Exception {
//
//		GetRequest getRequest = new GetRequest(INDEX, TYPE, noteId);
//
//		GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
//		Map<String, Object> resultMap = getResponse.getSource();
//
//		return objectMapper.convertValue(resultMap, Note.class);
//
//	}
//
//	private List<Note> getSearchResult(SearchResponse response) {
//
//		SearchHit[] searchHit = response.getHits().getHits();
//
//		List<Note> note = new ArrayList<>();
//
//		if (searchHit.length > 0) {
//
//			Arrays.stream(searchHit)
//					.forEach(hit -> note.add(objectMapper.convertValue(hit.getSourceAsMap(), Note.class)));
//		}
//
//		return note;
//	}
//
//	public List<Note> searchByTitle(String title) throws Exception {
//
//		SearchRequest searchRequest = new SearchRequest();
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//
//		QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title", title));
//
//		searchSourceBuilder.query(queryBuilder);
////                .nestedQuery("title",
////                        queryBuilder,
////                        ScoreMode.Avg));
//
//		searchRequest.source(searchSourceBuilder);
//
//		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
//
//		return getSearchResult(response);
//	}
//
//	public String deleteNote(String noteId) throws Exception {
//
//		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, noteId);// .index(INDEX).type(TYPE);
//		DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
//
//		return response.getResult().name();
//
//	}
//
//	public String updateNote(Note document) throws Exception {
//
//		Note resultDocument = findById(String.valueOf(document.getNoteId()));
//		Map<String, Object> documentMapper = objectMapper.convertValue(document, Map.class);
//
//		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(resultDocument.getNoteId()));
//
//		updateRequest.doc(documentMapper);
//
//		UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
//
//		return updateResponse.getResult().name();
//
//	}
//
//}
