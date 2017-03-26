package MyBServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import MyBDb.MyBConnector;
import MyBResult.ResultHelper;

/**
 * Servlet implementation class mybservlet.
 * 
 * @author Marcel
 */
@WebServlet("/mybservlet")
public class mybservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String USER_ID = "userId";

	private static final String USER_OP = "userOp";

	private static final String DOLOG = "doLog";

	private static final String ARRAY = "array";

	private static final String TEXT_PLAIN = "text/plain";

	enum Operation {
		PERFIL_GET, PERFIL_MAKE
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public mybservlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		final int operation = Integer.valueOf(request.getParameter(USER_OP));
		final boolean doLog = Boolean.valueOf(request.getParameter(DOLOG));
		final Operation op = Operation.values()[operation];
		final String userId = request.getParameter(USER_ID);
		final PrintWriter writer = response.getWriter();

		switch (op) {
		case PERFIL_MAKE:

			writer.write("user id "+userId);
			ExecutorService dbSaver = Executors.newFixedThreadPool(1);
			final ResultHelper resultHelper = new ResultHelper(userId,
					writer, doLog);
			resultHelper.startUserAnalysis();
//			dbSaver.execute(new Runnable() {
//
//				@Override
//				public void run() {
//				
//				}
//			});

			break;

		case PERFIL_GET:
			// writer.write("teste");
			MyBConnector mConnector = new MyBConnector();
			// writer.write(MyBConnector.getInstance().getProfile(userId));
			writer.write(mConnector.getProfile(userId));
			break;

		default:
			break;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		JSONObject responseObject = new JSONObject();
		response.setContentType(TEXT_PLAIN);

		try {
			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = request.getReader().readLine()) != null) {
				sb.append(s);
			}

			JSONObject object = new JSONObject(sb.toString());

			if (object != null) {
				final JSONArray array = object.getJSONArray(ARRAY);
				if (array != null) {

					MyBConnector mConnector = new MyBConnector();
					String responseString = mConnector.insertInstances(array);
					// String responseString = MyBConnector.getInstance()
					// .insertInstances(array);
					responseObject.put("count", responseString);
					response.getWriter().write(responseObject.toString());
				}
			}

		} catch (Exception e) {
			try {
				responseObject.put("response", "Error");
				response.getWriter().write(responseObject.toString());
			} catch (JSONException e1) {
				// Do nothing by now
			}
		}

	}
}
