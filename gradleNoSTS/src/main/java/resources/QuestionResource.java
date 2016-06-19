package resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.codahale.metrics.annotation.Timed;

import core.Question;
import databaseDAO.QuestionDAO;

@Path("/questions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
// class ParameterBean {
// @QueryParam("topic")
// public String[] topic;
//
// @QueryParam("size")
// public String[] size;
// }

public class QuestionResource {

	private QuestionDAO questionDAO;

	public QuestionResource(QuestionDAO dao) {
		questionDAO = dao;
	}

	@GET
	@Path("/{id}")
	@Timed
	public Question getQuestion(@PathParam("id") Integer id) {
		Question q = questionDAO.findQuestionById(id);
		if (q != null) {
			return q;
		} else {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	// @GET
	// @Timed
	// public List<Question> nebondaGet(@BeanParam ParameterBean paramBean) {
	// System.out.println(paramBean.size);
	// return getQuestions(paramBean.size, paramBean.topic);
	// }
	@GET
	@Timed
	public List<Question> nebondaGet(@QueryParam(value = "size") List<String> size,
			@QueryParam(value = "topic") List<String> topic) {

		System.out.println(size.size() + "      " + topic.size());
		if (size.size() > 0) {
			return getQuestions(size, topic);
		} else {
			return questionDAO.getAllQuestions();
		}

	}

	public List<Question> getQuestions(List<String> size, List<String> topic) {
		List<Question> qList = new ArrayList<Question>();
		for (int i = 0; i < size.size(); i++) {
			System.out.println("questions in topic are" + topic.get(i));
			List<Question> topicQuestions = questionDAO.getAllQuestionsByTopic(topic.get(i));
			System.out.println("questions  topic are" + topicQuestions.toString());

			// int[] qSubList = new Random().ints(1,
			// topicQuestions.size()).distinct().limit(Integer.parseInt(size.get(i)))
			// .toArray();
			// System.out.println("questions in topic are" +
			// qSubList.toString());
			for (int j = 0; j < Integer.parseInt(size.get(i)); j++) {
				qList.add(topicQuestions.get(j));
			}

		}
		return qList;
	}

	// public List<Question> listQuestions(String topic) {
	// return questionDAO.getAllQuestions(topic);
	// }
	//
	// public List<Question> getQuestionByIdList(String ids) {
	// String[] idList = ids.split(",");
	// final int[] ints = new Random().ints(1,
	// ).distinct().limit(Integer.parseInt(ids)).toArray();
	//// return Stream.of(idList) //
	//// .map(id -> questionDAO.findQuestionById(Integer.parseInt(id))) //
	//// .filter(q -> q != null) //
	//// .collect(Collectors.toList());
	// List<Question> qList = new ArrayList<Question>();
	// if (idList.length > 0) {
	// for (int i = 0; i < idList.length; i++) {
	// Question q =
	// questionDAO.findQuestionById(Integer.parseInt(idList[i]));
	// if (q != null) {
	// qList.add(q);
	// }
	// }
	// }
	// return qList;
	// }

	@POST
	@Timed
	public void saveQuestion(Question question) {
		if (question != null) {
			questionDAO.insertQuestion(question);
			throw new WebApplicationException(Response.Status.OK);
		} else {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

	}

	@PUT
	@Path("/{id}")
	public void updateQuestion(@PathParam("id") int id, Question question) {
		if (question != null) {
			questionDAO.updateQuestion(question, id);
			throw new WebApplicationException(Response.Status.OK);
		} else {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
	}

	@DELETE
	@Path("/{id}")
	@Timed
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
	public void deleteQuestion(@PathParam("id") Integer id) {

		if (questionDAO.findQuestionById(id) != null) {
			questionDAO.deleteQuestionById(id);
			throw new WebApplicationException(Response.Status.OK);
		} else {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	// @DELETE
	// @Path("/{question}")
	// @Timed
	// @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
	// MediaType.TEXT_PLAIN })
	// public void deleteQuestStr(@PathParam("question") String question) {
	//
	// if (quesDAO.findQuesByStr(question) != null) {
	// quesDAO.deleteQuesByStr(question);
	// throw new WebApplicationException(Response.Status.OK);
	// } else {
	// throw new WebApplicationException(Response.Status.NOT_FOUND);
	// }
	// }

}
