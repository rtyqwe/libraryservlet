import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PD extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		//
		// Register MySQL Paper driver
		//

		try {
			// register the MySQL Paper driver with DriverManager
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//
		// get the output stream for result page
		//

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {
			//
			// Connect to the database
			//

			Connection con = null;

			//
			// URL is jdbc:mysql:dbname
			// Change CS143 to the right database that you use
			//
			String url = "jdbc:mysql://localhost/cs143wch?zeroDateTimeBehavior=convertToNull";
			String userName = "cs143wch";
			String password = "9pvu4t2y";

			// connect to the database, user name and password can be specified
			// through this method
			con = DriverManager.getConnection(url, userName, password);

			//
			// Get operationID
			//
			String s_operationID = request.getParameter("operationID");
			int operationID = Integer.parseInt(s_operationID);

			if (operationID == 0)
			{
				Statement stmt = con.createStatement();
				ResultSet rs = null;

				out.println("<html>");
				out.println("<head>");
				String title = "Add Author";
				out.println("<title>" + title + "</title>");
				out.println("</head>");
				out.println("<body bgcolor=white>");
				out.println("<small>Adding author Information...</small>");
				
				//
				// Error flags
				// 	
				boolean wrongType = false;
				boolean missingField = false;
				boolean duplicateKey = false;
				boolean insertError = false;

				//
				// Get parameters from the form
				//
				String ID = request.getParameter("id");
				int int_ID = 0;
				if (ID != "" && ID != null)
				{
					try 
					{ int_ID = Integer.parseInt(ID); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }
				String author_name = request.getParameter("author_name");
				if (author_name == "" || author_name == null)
				{ missingField = true; }
				String preferred_name = request.getParameter("preferred_name");
				if (preferred_name == "" || preferred_name == null)
				{ missingField = true; }
				String first_name = request.getParameter("first_name");
				if (first_name == "" || first_name == null)
				{ missingField = true; }
				String last_name = request.getParameter("last_name");
				if (last_name == "" || last_name == null)
				{ missingField = true; }				
				
				// 
				// Check if the primary key already exist
				//
				if (!missingField && !wrongType)
				{
                        		rs = stmt.executeQuery("SELECT ID FROM Author WHERE ID=" + int_ID);
					if (rs != null && rs.isBeforeFirst())
					{ duplicateKey = true; }
				}
				
				//
				// Insert author's query
				//
				if (!missingField && !wrongType && !duplicateKey)
				{
					String insertQuery = "INSERT INTO Author VALUES(" + int_ID + ", '" + author_name + "', '" + preferred_name + "', '" +first_name + "', '" + last_name + "')";
					try
					{
						con.setAutoCommit(false);
						stmt.executeUpdate(insertQuery);
						con.commit();
						con.setAutoCommit(true);	
					}
					catch (SQLException ex)
					{
						System.err.println("SQLException: " + ex.getMessage());
						con.rollback();
						con.setAutoCommit(true);
						insertError = true;
					}
				}

				//
				// Reporting Errors	
				//
				if (missingField || wrongType || duplicateKey || insertError)
				{
					out.println("<p>There are error(s) when adding author:</p>");
					out.println("<ul>");
					if (missingField)
					{ out.println("<li>Some required fields(s) are missing.</li>"); }
					if (wrongType)
					{ out.println("<li>Please enter an integer for ID.</li>"); }
					if (duplicateKey)
					{ out.println("<li>The ID already exist. Please enter a different ID.</li>"); }
					if (insertError)
					{ out.println("<li>There was a problem inserting information. Please contact the web administrator.</li>"); }
					out.println("</ul>");
					out.println("<p>Please try again or return to the menu.</p>");
					out.println("<p><a href=\".././I1.html\">Try adding author again</a></p>");	
				}
				else
				{
					out.println("<p>Succesfully added author information.<p>");
					out.println("<p><a href=\".././I1.html\">Add more author</a></p>");
				}
				out.println("<p><a href=\".././index.html\">Go back to menu</a></p>");

				out.println("</body>");
				out.println("</html>");

				if (rs != null)
				{ rs.close(); }
				stmt.close();
			}

			else if (operationID == 1)
			{
				Statement stmt = con.createStatement();
				ResultSet rs = null;

				out.println("<html>");
				out.println("<head>");
				String title = "Add Coauthors";
				out.println("<title>" + title + "</title>");
				out.println("</head>");
				out.println("<body bgcolor=white>");
				out.println("<small>Adding Coauthors information...</small>");
				
				//
				// Error flags
				//
				boolean wrongType = false;
				boolean missingField = false;
				boolean duplicateKey = false;
				boolean a1IDNotExist = false;
				boolean a2IDNotExist = false;
				boolean sameAuthor = false;
				boolean notUnique = false;
				boolean pIDNotExist = false;
				boolean insertError = false;

				//
				// Get parameters from the form
				//
				String ID = request.getParameter("id");
				int int_ID = 0;
				if (ID != "" && ID != null)
				{
					try 
					{ int_ID = Integer.parseInt(ID); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }
				String author1ID = request.getParameter("author1ID");
				int int_author1ID = 0;
				if (author1ID != "" && author1ID != null)
				{
					try
					{ int_author1ID = Integer.parseInt(author1ID); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }
				String author2ID = request.getParameter("author2ID");
				int int_author2ID = 0;
				if (author2ID != "" && author2ID != null)
				{
					try
					{ int_author2ID = Integer.parseInt(author2ID); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }
				String paper_ID = request.getParameter("paper_ID");
				int int_paper_ID = 0;
				if (paper_ID != "" && paper_ID != null)
				{
					try
					{ int_paper_ID = Integer.parseInt(paper_ID); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }

				//
				// Check if primary key already exists
				// Check if author ID and paper ID exist or not
				// Check if author 1 ID and author 2 ID are the same or not
				//
				if (!missingField && !wrongType)
				{
                        		rs = stmt.executeQuery("SELECT ID FROM CoAuthored WHERE ID=" + int_ID);
					if (rs != null && rs.isBeforeFirst())
					{ duplicateKey = true; }
					rs = null;
					rs = stmt.executeQuery("SELECT ID FROM CoAuthored WHERE author1ID=" + int_author1ID + " AND author2ID=" + int_author2ID + " AND paper_ID=" + int_paper_ID);
					if (rs != null && rs.isBeforeFirst())
					{ notUnique = true; }
					rs = null;
					rs = stmt.executeQuery("SELECT ID FROM Author WHERE ID=" + int_author1ID);
					if (rs != null && !rs.isBeforeFirst())
					{ a1IDNotExist = true; }
					rs = null;
					rs = stmt.executeQuery("SELECT ID FROM Author WHERE ID=" + int_author2ID);
					if (rs != null && !rs.isBeforeFirst())
					{ a2IDNotExist = true; }
					rs = null;
					rs = stmt.executeQuery("SELECT paper_ID FROM Paper WHERE paper_ID=" + int_paper_ID);
					if (rs != null && !rs.isBeforeFirst())
					{ pIDNotExist = true; }
					if (int_author1ID == int_author2ID)
					{ sameAuthor = true; }
				}
				
				//
				// Insert coauthor's query
				// Also insert symmetric query at the next largest available ID
				//
				if (!missingField && !wrongType && !duplicateKey && !notUnique && !a1IDNotExist && !a2IDNotExist && !pIDNotExist && !sameAuthor)
				{
					try
					{	
						String insertQuery = "INSERT INTO CoAuthored VALUES(" + int_ID + ", " + int_author1ID + ", " + int_author2ID + ", " + int_paper_ID + ")";
						con.setAutoCommit(false);
						stmt.executeUpdate(insertQuery);
						rs = null;
						rs = stmt.executeQuery("SELECT ID FROM CoAuthored WHERE ID=(SELECT MAX(ID) FROM CoAuthored)");
						int nextAvailableID = 0;
						if (rs != null && rs.next())
						{ nextAvailableID = rs.getInt("ID")+1; }
						String symmetricQuery = "INSERT INTO CoAuthored VALUES(" + nextAvailableID + ", " + int_author2ID + ", " + int_author1ID + ", " + int_paper_ID + ")";
						stmt.executeUpdate(symmetricQuery);
						con.commit();
						con.setAutoCommit(true);	
					}
					catch (SQLException ex)
					{
						System.err.println("SQLException: " + ex.getMessage());
						con.rollback();
						con.setAutoCommit(true);
						insertError = true;
					}
				}
				
				//
				// Reporting Errors
				//
				if (missingField || wrongType || duplicateKey || notUnique || a1IDNotExist || a2IDNotExist || pIDNotExist || sameAuthor || insertError)
				{
					out.println("<p>There are error(s) when adding coauthors:</p>");
					out.println("<ul>");
					if (missingField)
					{ out.println("<li>Some required fields(s) are missing.</li>"); }
					if (wrongType)
					{ out.println("<li>Please enter an integer for every field.</li>"); }
					if (duplicateKey)
					{ out.println("<li>The ID already exist. Please enter a different ID.</li>"); }
					if (notUnique)
					{ out.println("<li>The combination of <i>author 1 ID</i>, <i>author 2 ID</li>, and <i>paper ID</i> has to be unique. Please enter another combination.</li>"); }
					if (a1IDNotExist)
					{ out.println("<li>This <i>author 1 ID</i> does not exist. Please enter an existing ID, or add a ID for this new author.</li>"); }
					if (a2IDNotExist)
					{ out.println("<li>This <i>author 2 ID</i> does not exist. Please enter an existing ID, or add a ID for this new author.</li>"); }
					if (pIDNotExist)
					{ out.println("<li>This <i>paper ID</i> does not exist. Please enter an existing ID, or add a ID for this new paper.</li>"); }
					if (sameAuthor)
					{ out.println("<li><i>author 1 ID</i> and <i>author 2 ID</i> cannot be the same.</li>"); }
					if (insertError)
					{ out.println("<li>There was a problem inserting information. Please contact the web administrator.</li>"); }
					out.println("</ul>");
					out.println("<p>Please try again or return to the menu.</p>");
					out.println("<p><a href=\".././I1.html\">Try adding coauthors again</a></p>");	
				}
				else
				{
					out.println("<p>Succesfully added coauthors information.<p>");
					out.println("<p><a href=\".././I1.html\">Add more coauthors</a></p>");
				}
				out.println("<p><a href=\".././index.html\">Go back to menu</a></p>");

				out.println("</body>");
				out.println("</html>");

				if (rs != null)
				{ rs.close(); }
				stmt.close();
			}

			else if (operationID == 2)
			{	
				Statement stmt = con.createStatement();
				ResultSet rs = null;

				out.println("<html>");
				out.println("<head>");
				String title = "Add Citations";
				out.println("<title>" + title + "</title>");
				out.println("</head>");
				out.println("<body bgcolor=white>");
				out.println("<small>Adding Citations...</small>");
				
				//
				// Error flags
				//
				boolean wrongType = false;
				boolean missingField = false;
				boolean duplicateKey = false;
				boolean p1IDNotExist = false;
				boolean p2IDNotExist = false;
				boolean selfCiteError = false;
				boolean insertError = false;

				//
				// Get parameters from the form
				//
				String ID = request.getParameter("id");
				int int_ID = 0;
				if (ID != "" && ID != null)
				{
					try 
					{ int_ID = Integer.parseInt(ID); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }
				String paper1ID = request.getParameter("paper1ID");
				int int_paper1ID = 0;
				if (paper1ID != "" && paper1ID != null)
				{
					try
					{ int_paper1ID = Integer.parseInt(paper1ID); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }
				String paper2ID = request.getParameter("paper2ID");
				int int_paper2ID = 0;
				if (paper2ID != "" && paper2ID != null)
				{
					try
					{ int_paper2ID = Integer.parseInt(paper2ID); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }
				String is_self_citation = request.getParameter("is_self_citation");
				int int_is_self_citation = 0;
				if (is_self_citation != "" && is_self_citation != null)
				{
					try
					{ int_is_self_citation = Integer.parseInt(is_self_citation); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }
				
				//
				// Check if primary key already exists
				// Check if paper IDs exist or not, and check for is_self_citation value
				//
				if (!missingField && !wrongType)
				{
                        		rs = stmt.executeQuery("SELECT ID FROM Cites WHERE ID=" + int_ID);
					if (rs != null && rs.isBeforeFirst())
					{ duplicateKey = true; }
					rs = null;
					rs = stmt.executeQuery("SELECT ID FROM Paper WHERE ID=" + int_paper1ID);
					if (rs != null && !rs.isBeforeFirst())
					{ p1IDNotExist = true; }
					rs = null;
					rs = stmt.executeQuery("SELECT ID FROM Paper WHERE ID=" + int_paper2ID);
					if (rs != null && !rs.isBeforeFirst())
					{ p2IDNotExist = true; }
					if (int_is_self_citation != 1 && int_is_self_citation != 0)
					{ selfCiteError = true; }
				}
				
				//
				// Insert query to add citations
				//
				if (!missingField && !wrongType && !duplicateKey && !p1IDNotExist && !p2IDNotExist && !selfCiteError)
				{
					try
					{	
						String insertQuery = "INSERT INTO Cites VALUES(" + int_ID + ", " + int_paper1ID + ", " + int_paper2ID + ", " + int_is_self_citation + ")";
						con.setAutoCommit(false);
						stmt.executeUpdate(insertQuery);
						con.commit();
						con.setAutoCommit(true);	
					}
					catch (SQLException ex)
					{
						System.err.println("SQLException: " + ex.getMessage());
						con.rollback();
						con.setAutoCommit(true);
						insertError = true;
					}
				}
				
				//
				// Reporting Errors
				//
				if (missingField || wrongType || duplicateKey || p1IDNotExist || p2IDNotExist || selfCiteError  || insertError)
				{
					out.println("<p>There are error(s) when adding citations:</p>");
					out.println("<ul>");
					if (missingField)
					{ out.println("<li>Some required fields(s) are missing.</li>"); }
					if (wrongType)
					{ out.println("<li>Please enter an integer for every field.</li>"); }
					if (duplicateKey)
					{ out.println("<li>The ID already exist. Please enter a different ID.</li>"); }
					if (p1IDNotExist)
					{ out.println("<li>This <i>paper 1 ID</i> does not exist. Please enter an existing ID, or add a ID for this new paper.</li>"); }
					if (p2IDNotExist)
					{ out.println("<li>This <i>paper 2 ID</i> does not exist. Please enter an existing ID, or add a ID for this new paper.</li>"); }
					if (selfCiteError)
					{ out.println("<li>Please enter 0 or 1 for <i>is self citation</i> field.</li>"); }
					if (insertError)
					{ out.println("<li>There was a problem inserting information. Please contact the web administrator.</li>"); }
					out.println("</ul>");
					out.println("<p>Please try again or return to the menu.</p>");
					out.println("<p><a href=\".././I2.html\">Try adding citations again</a></p>");	
				}
				else
				{
					out.println("<p>Succesfully added citations.<p>");
					out.println("<p><a href=\".././I2.html\">Add more citations</a></p>");
				}
				out.println("<p><a href=\".././index.html\">Go back to menu</a></p>");

				out.println("</body>");
				out.println("</html>");

				if (rs != null)
				{ rs.close(); }
				stmt.close();
			}

			else if (operationID == 3)
			{
				Statement stmt = con.createStatement();
				ResultSet rs = null;

				out.println("<html>");
				out.println("<head>");
				String title = "Add Comments to Paper";
				out.println("<title>" + title + "</title>");
				out.println("</head>");
				out.println("<body bgcolor=white>");
								
				//
				// Check if comment is submitted or not. If it is, then process it.
				//
				String comments_submit = request.getParameter("comments_submit");
				if (comments_submit != null)
				{
					out.println("<small>Adding comments...</small>");
					boolean updateError = false;
					String comments_str = request.getParameter("comments_str");
					if (comments_str == "")
					{ comments_str = null; }
					String paper_ID = request.getParameter("paper_ID");
					int int_paper_ID = Integer.parseInt(paper_ID);
					try
					{	
						String updateQuery = "UPDATE Paper SET comments_str = ? WHERE ID = ?";
						con.setAutoCommit(false);
						PreparedStatement pstmt = con.prepareStatement(updateQuery);
						pstmt.setString(1, comments_str);
						pstmt.setInt(2, int_paper_ID);
						pstmt.executeUpdate();
						con.commit();
						con.setAutoCommit(true);	
					}
					catch (SQLException ex)
					{
						System.err.println("SQLException: " + ex.getMessage());
						con.rollback();
						con.setAutoCommit(true);
						updateError = true;
					}
					if (updateError)
					{
						out.println("<p>There was a problem updating comments. Please contact the web administrator.</p>"); 
						out.println("<p>Please try again or return to the menu.</p>");
						out.println("<p><a href=\".././I3.html\">Try adding comments again</a></p>");
					}
					else
					{
						out.println("<p>Succesfully added comments.<p>");
						out.println("<p><a href=\".././I3.html\">Add more comments</a></p>");
					}
				}
				
				else
				{
					out.println("<small>Requesting paper...</small>");

					//
					// Error flags
					// 	
					boolean wrongType = false;
					boolean missingField = false;
					boolean pIDNotExist = false;

					//
					// Get parameters from the form
					//
					String paper_ID = request.getParameter("paper_ID");
					int int_paper_ID = 0;
					if (paper_ID != "" && paper_ID != null)
					{
						try 
						{ int_paper_ID = Integer.parseInt(paper_ID); }
						catch (Exception e)
						{ wrongType = true; }
					}
					else
					{ missingField = true; }
					
					//
					// Check if paper ID exist or not
					//
					if (!missingField && !wrongType)
					{
						rs = stmt.executeQuery("SELECT ID FROM Paper WHERE ID=" + int_paper_ID);
						if (rs != null && !rs.isBeforeFirst())
						{ pIDNotExist = true; }
					}
	
					//
					// Reporting Errors
					//
					if (missingField || wrongType || pIDNotExist)
					{
						out.println("<p>There are error(s) when requesting for paper:</p>");
						out.println("<ul>");
						if (missingField)
						{ out.println("<li>The <i>paper ID </i>  field is missing. Please fill it out.</li>"); }
						if (wrongType)
						{ out.println("<li>Please enter an integer for <i>paper ID</i>.</li>"); }
						if (pIDNotExist)
						{ out.println("<li>This <i>paper ID</i> does not exist. Please enter an existing ID, or add a ID for this new paper.</li>"); }
						out.println("</ul>");
						out.println("<p>Please try again or return to the menu.</p>");
						out.println("<p><a href=\".././I3.html\">Try adding comments again</a></p>");	
					}
					else
					{
						String existingComments = "";
	
						rs = null;
						rs = stmt.executeQuery("SELECT comments_str FROM Paper WHERE ID=" + int_paper_ID);
							
						if (rs != null && rs.next())
						{ 
							existingComments = rs.getString("comments_str") == null ? existingComments : rs.getString("comments_str"); 
						}
	
						out.println("<form method=get actin=servlet/PD>");
						out.println("<h3> Add/Edit Comments to this Paper</h3>");
						out.println("<p>");
						out.println("<label for=\"comments_str\">Comments:</label></br>");
						out.println("<textarea name=\"comments_str\" cols=\"60\" rows=\"8\" maxlength=280>" + existingComments + "</textarea></br>");
						out.println("<small>Character limit: 280</small>");
						out.println("</p>");
						out.println("<input type=\"hidden\" name=\"operationID\" value=\"3\">");
						out.println("<input type=\"hidden\" name=\"paper_ID\" value=\"" + int_paper_ID + "\">");
						out.println("<p> <input type=submit name=\"comments_submit\" value=\"Submit\"> </p>");
						out.println("<p><a href=\".././I3.html\">Go back to comment</a></p>");
					}
				}
				out.println("<p><a href=\".././index.html\">Go back to menu</a></p>");
				
				out.println("</body>");
				out.println("</html>");
	
				if (rs != null)
				{ rs.close(); }
				stmt.close();
			}

			else if (operationID == 4)
			{
				Statement stmt = con.createStatement();
				ResultSet rs = null;

				out.println("<html>");
				out.println("<head>");
				String title = "Add Papers";
				out.println("<title>" + title + "</title>");
				out.println("</head>");
				out.println("<body bgcolor=white>");
				out.println("<small>Adding paper...</small>");
				
				//
				// Error flags
				// 	
				boolean wrongType = false;
				boolean missingField = false;
				boolean dateError = false;
				boolean duplicateID = false;
				boolean duplicatePID = false;
				boolean authorNotExist = false;
				boolean dupAuthor = false;
				boolean insertError = false;

				//
				// Get parameters from the form
				//
				String ID = request.getParameter("id");
				int int_ID = 0;
				if (ID != "" && ID != null)
				{
					try 
					{ int_ID = Integer.parseInt(ID); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }

				String paper_ID = request.getParameter("paper_ID");
				int int_paper_ID = 0;
				if (paper_ID != "" && paper_ID != null)
				{
					try 
					{ int_paper_ID = Integer.parseInt(paper_ID); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }

				String title_str = request.getParameter("title_str");
				if (title_str == "" || title_str == null)
				{ missingField = true; }

				String authors_str = request.getParameter("authors_str");
				if (authors_str == "" || authors_str == null)
				{ missingField = true; }

				String area = request.getParameter("area");
				if (area == "" || area == null)
				{ area = null; }

				String num_abstract_wds = request.getParameter("num_abstract_wds");
				int int_num_abstract_wds = 0;
				if (num_abstract_wds != "" && num_abstract_wds != null)
				{
					try
					{ int_num_abstract_wds = Integer.parseInt(num_abstract_wds); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }

				String num_kb = request.getParameter("num_kb");
				int int_num_kb = 0;
				if (num_kb != "" && num_kb != null)
				{
					try
					{ int_num_kb = Integer.parseInt(num_kb); }
					catch (Exception e)
					{ wrongType = true; } 
				}
				else
				{ num_kb = null; }
			
				String num_pages = request.getParameter("num_pages");
				int int_num_pages = 0;
				if (num_pages != "" && num_pages != null)
				{
					try
					{ int_num_pages = Integer.parseInt(num_pages); }
					catch (Exception e)
					{ wrongType = true; }
				}
				{ num_pages = null; }
				
				String num_revisions = request.getParameter("num_revisions");
				int int_num_revisions = 0;
				if (num_revisions != "" && num_revisions != null)
				{
					try
					{ int_num_revisions = Integer.parseInt(num_revisions); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }

				String num_title_wds = request.getParameter("num_title_wds");
				int int_num_title_wds = 0;
				if (num_title_wds != "" && num_title_wds != null)
				{
					try
					{ int_num_title_wds = Integer.parseInt(num_title_wds); }
					catch (Exception e)
					{ wrongType = true; }
				}
				else
				{ missingField = true; }

				String comments_str = request.getParameter("comments_str");
				if (comments_str == "" || comments_str == null)
				{ comments_str = null; }
				
				String submit_date = request.getParameter("submit_date");
				java.sql.Date sqlDate = null;
				if (submit_date == "" || submit_date == null)
				{ submit_date = null; }
				else
				{
					try
					{
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						java.util.Date date = (java.util.Date)formatter.parse(submit_date);
						sqlDate = new java.sql.Date(date.getTime());
					}
					catch (Exception e)
					{
						dateError = true;
					}
				}

				String submitter_email = request.getParameter("submitter_email");
				if (submitter_email == "" || submitter_email == null)
				{ submitter_email = null; }

				String submitter_name = request.getParameter("submitter_name");
				if (submitter_name == "" || submitter_name == null)
				{ submitter_name = null; }
				
				//
				// Check if ID and paper ID already exist or not
				//
				if (!missingField && !wrongType && !dateError)
				{
					rs = stmt.executeQuery("SELECT ID FROM Paper WHERE ID=" + int_ID);
					if (rs != null && rs.isBeforeFirst())
					{ duplicateID = true; }
					rs = null;
					rs = stmt.executeQuery("SELECT paper_ID FROM Paper WHERE paper_ID=" + int_paper_ID);
					if (rs != null && rs.isBeforeFirst())
					{ duplicatePID = true; }
				}
				
				//
				// Check for existence of authors and duplicates.
				//
				int authors_size = 0;
				ArrayList<String> authors_missing = new ArrayList<String>();
				if (!missingField && !wrongType && !dateError && !duplicateID && !duplicatePID)
				{
					ArrayList<String> authors_list = new ArrayList<String>();	
					Collections.addAll(authors_list, authors_str.split(",\\s*"));
					authors_size = authors_list.size();
					int[] AID_arr = new int[authors_size];
					
					for (int i = 0; i < authors_size; i++)
					{
						rs = null;
						rs = stmt.executeQuery("SELECT ID, author_name FROM Author WHERE author_name='" + authors_list.get(i) + "'");
						if (rs != null)
						{ 
							if (!rs.isBeforeFirst())
							{ 
								authors_missing.add(authors_list.get(i)); 
								authorNotExist = true;
							}
							else
							{ 
								rs.next();
								AID_arr[i] = rs.getInt("ID"); 
							}
						}
					}
					
					Set<String> authors_set = new HashSet<String>(authors_list);
					if (authors_set.size() < authors_list.size())
					{ dupAuthor = true; }
				
					//
					// Insert Paper information along with the related CoAuthored and Authored information
					//
					if (!authorNotExist && !dupAuthor)
					{
						try
						{
							String insertQuery = "INSERT INTO Paper(ID, paper_ID, title_str, authors_str, area, num_abstract_wds, num_authors, num_kb, num_pages, num_revisions, num_title_wds, comments_str, submit_date, submitter_email, submitter_name) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";			
							con.setAutoCommit(false);
							PreparedStatement pstmt = con.prepareStatement(insertQuery);
							pstmt.setInt(1, int_ID);
							pstmt.setInt(2, int_paper_ID);
							pstmt.setString(3, title_str);
							pstmt.setString(4, authors_str);
							pstmt.setString(5, area);
							pstmt.setInt(6, int_num_abstract_wds);
							pstmt.setInt(7, authors_size);
							if (num_kb != null)
								pstmt.setInt(8, int_num_kb);
							else
								pstmt.setNull(8, java.sql.Types.INTEGER); 
							if (num_pages != null)
								pstmt.setInt(9, int_num_pages);
							else
								pstmt.setNull(9, java.sql.Types.INTEGER);
							pstmt.setInt(10, int_num_revisions);
							pstmt.setInt(11, int_num_title_wds);
							pstmt.setString(12, comments_str);
							pstmt.setDate(13, sqlDate);
							pstmt.setString(14, submitter_email);
							pstmt.setString(15, submitter_name);
							pstmt.executeUpdate();
							con.commit();
							con.setAutoCommit(true);
						}
						catch (SQLException ex)
						{
							System.err.println("SQLException:  " + ex.getMessage());
							con.rollback();
							con.setAutoCommit(true);
							insertError = true;	
						}

						if (!insertError)
						{
							rs = null;
							rs = stmt.executeQuery("SELECT ID FROM Authored WHERE ID=(SELECT MAX(ID) FROM Authored)");
							int nextAvailableAID = 0;
							if (rs != null && rs.next())
							{ nextAvailableAID = rs.getInt("ID")+1; }
					
							for (int i = 0; i < authors_size && !insertError; i++)
							{
								try
								{
									con.setAutoCommit(false);
									stmt.executeUpdate("INSERT INTO Authored VALUES(" + nextAvailableAID + ", " + AID_arr[i] + ", " + int_ID + ", null, " + (i+1) + ", '" + authors_list.get(i) + "', null, null, null, null)");
									nextAvailableAID++;
									con.commit();
									con.setAutoCommit(true);
								}
								catch (SQLException ex)
								{
									System.err.println("SQLException: " + ex.getMessage());
									con.rollback();
									con.setAutoCommit(true);
									insertError = true;
								}
							}
						
							rs = null;
							rs = stmt.executeQuery("SELECT ID FROM CoAuthored WHERE ID=(SELECT MAX(ID) FROM CoAuthored)");
							int nextAvailableCID = 0;
							if (rs != null & rs.next())
							{ nextAvailableCID = rs.getInt("ID")+1; }
	
							for (int i = 0; i < authors_size; i++)
							{
								for (int j = 0; j < authors_size && !insertError; j++)
								{
									if (i != j)
									{
										try
										{
											con.setAutoCommit(false);
											stmt.executeUpdate("INSERT INTO CoAuthored VALUES(" + nextAvailableCID + ", " + AID_arr[i] + ", " + AID_arr[j] + ", " + int_paper_ID + ")");
											nextAvailableCID++;
											con.commit();
											con.setAutoCommit(true);
										}
										catch (SQLException ex)
										{
											System.err.println("SQLException: " + ex.getMessage());
											con.rollback();
											con.setAutoCommit(true);
											insertError = true;
										}
									}
								}
							}
						}			
					}
				}
				if (wrongType || missingField || dateError || duplicateID || duplicatePID || authorNotExist || dupAuthor || insertError)
				{
					out.println("<p>There are error(s) when adding paper:</p>");
					out.println("<ul>");
					if (missingField)
					{ out.println("<li>Some required fields(s) are missing.</li>"); }
					if (wrongType)
					{ out.println("<li>Please enter an integer for fields that required integer values.</li>"); }
					if (dateError)
					{ out.println("<li>Please enter the <i>Submit Date</i> in YYYY-MM-DD format.</li>"); }
					if (duplicateID)
					{ out.println("<li>The <i>submission ID</i> already exist. Please enter a different ID.</li>"); }
					if (duplicatePID)
					{ out.println("<li>This <i>paper ID</i> already exist. Please enter a different ID.</li>"); }
					if (authorNotExist)
					{ 
						out.println("<li>The following author(s) do not exist in the database. Please enter existing author(s).");
						out.println("<ul>");
						for (int i = 0; i < authors_missing.size(); i++)
						{
							out.println("<li>" + authors_missing.get(i) + "</li>");
						}
						out.println("</ul></li>");
					}
					if (dupAuthor)
					{ out.println("<li>There are duplicate authors in the input. Please enter authors without duplicate.</li>"); }
					if (insertError)
					{ out.println("<li>There was a problem inserting information. Please contact the web administrator.</li>"); }
					out.println("</ul>");
					out.println("<p>Please try again or return to the menu.</p>");
					out.println("<p><a href=\".././I4.html\">Try adding papers again</a></p>");	
				}
				else
				{
					out.println("<p>Succesfully added paper.<p>");
					out.println("<p><a href=\".././I4.html\">Add more papers</a></p>");
				}
				out.println("<p><a href=\".././index.html\">Go back to menu</a></p>");

				out.println("</body>");
				out.println("</html>");

				if (rs != null)
				{ rs.close(); }
				stmt.close();
			}
			else if (operationID == 5){

             
                //
                // Execute a SQL statement and print out the result
                //

                // Execute a SQL statement
                Statement stmt = con.createStatement();
            
                
                String custom_author = request.getParameter("custom_author"); //changed parameter name for custom_author option
                String citation = request.getParameter("citation"); //changed parameter name for select options
                String paperID = request.getParameter("paperID"); //changed parameter name for select options
                String custom_paper = request.getParameter("custom_paper");
                ResultSet rs = null;
                ResultSet rs2 = null;
                ResultSet rs3 = null;
                ResultSet rs6 = null;
                String temp_query = null;
                String find_papers = null;
                String cites_query = null;
                String citedby_query = null;
              
                PreparedStatement pst = null;
                PreparedStatement pst2= null;
                ResultSet rs_papers = null;
                String custom_paperIDs = "";
                    
                if (custom_author != null){
                    find_papers = "SELECT paperID FROM Authored where AuthorID ="+custom_author;
                  //  temp_query = "SELECT ID, paper_id, title_str, authors_str, area, num_pages, comments_str FROM Paper as P WHERE P.ID in (SELECT Aed.paperID FROM Authored as Aed where Aed.AuthorID ="+custom_author+")";
                    try {
                        rs_papers = stmt.executeQuery(find_papers);
                        
                        int count = 0;
                        while(rs_papers.next()){
                            if(count==0)
                                custom_paperIDs = Integer.toString(rs_papers.getInt(1));
                            else
                            custom_paperIDs = custom_paperIDs+","+Integer.toString(rs_papers.getInt(1));
                            count=1;
                        }
                        } catch (SQLException e){
                            e.printStackTrace();
                            }
                          //  out.println(custom_paperIDs);
                    try {               
                        rs = stmt.executeQuery("SELECT ID, paper_id, title_str, authors_str, area, num_pages, comments_str FROM Paper where ID in ("+custom_paperIDs+")");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else if(citation!=null && citation.equals("true")){
                   String find_cites_papers = "SELECT paper2ID FROM Cites where paper1ID ="+paperID;
                    String find_citedby_papers = "SELECT paper1ID FROM Cites where paper2ID ="+paperID;
                  //  temp_query = "SELECT ID, paper_id, title_str, authors_str, area, num_pages, comments_str FROM Paper as P WHERE P.ID in (SELECT Aed.paperID FROM Authored as Aed where Aed.AuthorID ="+custom_author+")";
                         ResultSet find_cites = null;
                         ResultSet find_citedby = null;
                         pst2 = con.prepareStatement(find_cites_papers);
                         find_cites = pst2.executeQuery();
                         pst2 = con.prepareStatement(find_citedby_papers);
                        find_citedby = pst2.executeQuery();
                       
                        int count = 0;
                        String cited_ID = "";
                        String citedby_ID = "";
                        while(find_cites!=null &&find_cites.next()){
                            if(count==0)
                                cited_ID = Integer.toString(find_cites.getInt(1));
                            else
                            cited_ID= cited_ID+","+Integer.toString(find_cites.getInt(1));
                            count=1;
                        }
                        count = 0;
                        while(find_citedby!=null &&find_citedby.next()){
                            if(count==0)
                                citedby_ID = Integer.toString(find_citedby.getInt(1));
                            else
                            citedby_ID = citedby_ID+","+Integer.toString(find_citedby.getInt(1));
                            count=1;
                        }
                        
                    temp_query = "SELECT ID, title_str, authors_str FROM Paper WHERE ID ="+paperID;
                   try {
                        PreparedStatement pst3= con.prepareStatement(temp_query);
                        
                        rs6=pst3.executeQuery();
                    } catch (SQLException e){
                        e.printStackTrace();
                        }
                if(cited_ID!=null){
                    cites_query = "SELECT title_str,ID FROM Paper as P WHERE P.ID IN ("+cited_ID+")";
                    try {
                       
                        PreparedStatement pst4= con.prepareStatement(cites_query);
                        rs2 = pst4.executeQuery();
                        
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                else 
                    rs2 =null;
                if(citedby_ID!=null){

                    citedby_query = "SELECT title_str,ID FROM Paper as P WHERE P.ID IN ("+citedby_ID+")";
                    try {
                         pst2 = con.prepareStatement(citedby_query);
                        rs3 = pst2.executeQuery();
                    }
                    catch (SQLException e){
                    e.printStackTrace();
                    }
                    }
                    else rs3 = null;
                }
                else if (custom_paper!=null){
                    try {
                        rs = stmt.executeQuery("SELECT ID, paper_id, title_str, authors_str, area, num_pages, comments_str FROM Paper where ID = "+custom_paper);
                    } catch (SQLException e){
                        e.printStackTrace();
                        }
                }
                else{
                    try {               
                        rs = stmt.executeQuery("SELECT ID, paper_id, title_str, authors_str, area, num_pages, comments_str FROM Paper");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // print out the result
                out.println("<html>");
                out.println("<head>");
                String title = "Paper Information";
                out.println("<title>" + title + "</title>");
                out.println("</head>");
                out.println("<body bgcolor=white>");
                out.println("<h1>" + title + "</h1>");
                out.println("<p><a href=.././index.html>Back to menu</a></p>");
                out.println("Received results: <p>");
                
                out.println("<table border=\"1\">");
                
                if(citation!=null &&citation.equals("true")){
                    out.println("<tr><th>ID</th><th>title_str</th><th>Authors</th><th>Cites</th><th>Cited by</th></tr>");
                 }
                else
                    out.println("<tr><th>ID</th><th>paper_id</th><th>title_str</th><th>Authors</th><th>area</th><th>num_pages</th><th>comments_str</th><th>Citation Info</th></tr>");
                //Modified HTML table with respective column names of Paper
                
                // prrint the result set
                // rs.next() returns false when there are no more rows
                int i=2;
                if(citation!=null &&citation.equals("true")){
                   rs6.next();
                   int id = rs6.getInt(1);

                    String title_str = rs6.getString(2);

                    String authors_str = rs6.getString(3);
                
                    String cites_titles = "";
                    String citedby_titles = "";
                    
                  while (rs2!=null&&rs2.next()){
                        cites_titles = cites_titles+ "<a href=../servlet/PD?operationID=5&custom_paper="+rs2.getString(2)+">"+rs2.getString(1)+"</a><p>";
                    }
                    if(rs2!=null)rs2.close();
              
                    while (rs3!=null && rs3.next()){
                        citedby_titles = citedby_titles+ "<a href=../servlet/PD?operationID=5&custom_paper="+rs3.getString(2)+">"+rs3.getString(1)+"</a><p>";
                    }
                     if(rs3!=null)rs3.close();
                    
                    out.println("<tr><td>" + id + "</td><td>"  + title_str + "</td><td>"
                     + authors_str + "</td><td>" + cites_titles + "</td><td>" +citedby_titles+"</td></tr>");
                     
                }
                else{
                while (rs.next()) {
                    //Declared variables to save results from queries, according to their columns and attributes
                    int id = rs.getInt(1);
                    int paper_id = rs.getInt(2);
                    String title_str = rs.getString(3);
                    String authors_str= rs.getString(4);
                    String area = rs.getString(5);
                    int num_pages = rs. getInt(6);
                    String comments_str = rs.getString(7);
                    
                    
                    //Query for linking each paper_id to its authors through Authored
                    String paper_id_str = Integer.toString(id);
                    
                    String q_Authored = "select author.author_name, author.ID from Author as author join Authored as authored where authored.paperID="+paper_id_str+" and author.ID=authored.AuthorID";
                    String authors="";
                   //out.println(q_Authored);
                   
                    ResultSet rs5 = null;
                    pst = con.prepareStatement(q_Authored);
                    rs5 = pst.executeQuery();
                
                    

                     int j = 0;
                     while(rs5.next())
                     {
                        
                        if(j==0){
    //need to change back for PD.java                        
                            authors = "<a href=../servlet/PD?operationID=5&custom_author="+rs5.getString(2)+">"+rs5.getString(1)+"</a>";
                        }
                        else
     //need to change back for PD.java
                            authors = authors+ "<br>"+"<a href=../servlet/PD?operationID=5&custom_author="+rs5.getString(2)+">"+rs5.getString(1)+"</a>";
                        j++;
                     }
                      rs5.close();
                     
                  //   out.println(authors);
                     
                     //Print out the record
                     out.println("<tr><td>" + id + "</td><td>" + paper_id
                     + "</td><td>" + title_str + "</td><td>"
                     + authors + "</td><td>" + area + "</td><td>" +num_pages+"</td><td>"
                     + comments_str +  "</td><td><a href=../servlet/PD?operationID=5&citation=true&paperID="+id+">Show</a></td></tr>");
                    
                }
            }
                out.println("</table>");
               
                out.println("</body>");
                out.println("</html>");
                
                if(rs6!=null)
                rs6.close();
                stmt.close();

			}
			else if (operationID == 6){
			String custom_author = request.getParameter("custom_author");
				Statement stmt = con.createStatement();
				out.println("<html>");
                out.println("<head>");
                String title = "Author Information";
                out.println("<title>" + title + "</title>");
                out.println("</head>");
                out.println("<body bgcolor=white>");
                out.println("<h1>" + title + "</h1>");
                out.println("<p><a href=.././index.html>Back to menu</a></p>");
                out.println("Received results:<p>");
                
                out.println("<table border=\"1\">");
                out.println("<tr><th>Author ID</th><th>Name</th><th>Co-Authors</th><th>Papers written</th></tr>");
                //Modified HTML table with respective column names of Paper

				Statement stmt3 = con.createStatement();
                ResultSet rs3 = null;
                if(custom_author!=null){
                    rs3 = stmt.executeQuery("select A.ID, A.author_name from Author as A where A.ID ="+custom_author);
                    
                }
				else 
                    rs3 = stmt.executeQuery("select A.ID, A.author_name from Author as A");
			//	out.println(custom_author);
				ResultSet rs5 = null;
 				while (rs3.next()) {

 					int id_author = rs3.getInt(1);
 					String author_name = rs3.getString(2);

 					String author_co = "";
 					int j = 1;
                    String find_coauthor_q = "select distinct author2ID from CoAuthored as C where C.author1ID="+id_author;
                    //out.println(find_coauthor_q+"<p>");
                    PreparedStatement find_coauthor = con.prepareStatement(find_coauthor_q);
                    ResultSet coauthors = find_coauthor.executeQuery();
                    int count = 0;
                    String coauthors_id = "";
                    while(coauthors.next()){
                            if(count==0)
                                coauthors_id = Integer.toString(coauthors.getInt(1));
                            else
                            coauthors_id= coauthors_id+","+Integer.toString(coauthors.getInt(1));
                            count=1;
                        }
                   // out.println(coauthors_id+"<p>");

                    if(("").equals(coauthors_id)==false){
                        
                    PreparedStatement pst = con.prepareStatement("select author_name, ID from Author as A where A.ID in ("+coauthors_id+")");
 				
                    ResultSet rs4 = pst.executeQuery();
 					while(rs4.next()){
 						
 							author_co = author_co+"<a href=../servlet/PD?operationID=6&custom_author="+rs4.getString(2)+">"+rs4.getString(1)+"</a><p>";

 					}
                }
                   
                    String find_paper_q="SELECT A.paperID FROM Authored as A WHERE A.AuthorID = "+id_author;
                    PreparedStatement find_paper_written = con.prepareStatement(find_paper_q);
                    ResultSet paper_written = find_paper_written.executeQuery();
                    String paper_written_output = "";
                    count = 0;
                     String paper_written_id = "";
                     while(paper_written.next()){
                        if(count==0)
                            paper_written_id = Integer.toString(paper_written.getInt(1));
                        else 
                            paper_written_id = paper_written_id+","+Integer.toString(paper_written.getInt(1));
                        count = 1;
                        }
                     if(("").equals(paper_written_id)==false){
                        PreparedStatement pst = con.prepareStatement("select P.title_str, ID from Paper as P where P.ID in ("+paper_written_id+")");
                        ResultSet rs4 = pst.executeQuery();
                        while(rs4.next()){
                            paper_written_output = paper_written_output+"<a href=../servlet/PD?operationID=5&custom_paper="+rs4.getString(2)+">"+rs4.getString(1)+"</a><p>";

                        }
                    }
                    out.println("<tr><td>" + id_author + "</td><td>" + author_name
                     + "</td><td>" + author_co +  "</td><td>"+paper_written_output+"</td></tr>");

               		
                }
                rs3.close();
                stmt3.close();
               
                out.println("</table>");
               	
                out.println("</body>");
                out.println("</html>");
			}
			else if(operationID == 7){
			
                //
                // Error flags
                //  
     
                boolean missingField = false;
                
                // Execute a SQL statement
                Statement stmt = con.createStatement();
                

				String param = request.getParameter("dropdown"); //changed parameter name for select options
                String keyword = request.getParameter("keyword");
                if(keyword=="" || keyword==null){
                    missingField = true;
                }

                // print out the result
                
                ResultSet rs = null;
                 if(param.equals("Author")){
                    
                    if(missingField){
                        out.println("<li>Search keyword is missing.</li>");
                    }
                    else{
                          out.println("<html>");
                    out.println("<head>");
                    String title = "Search Results for keyword \""+keyword+"\""+ " in Author:";
                    out.println("<title>" + title + "</title>");
                    out.println("</head>");
                    out.println("<body bgcolor=white>");
                    out.println("<h1>" + title + "</h1>");
                
                        out.println("Received results:<p>");
                        out.println("<p><a href=.././index.html>Back to menu</a></p>");
                        out.println("<table border=\"1\">");
                        out.println("<tr><th>ID</th><th>Author Name</th><th>Preferred Name</th><th>First Name</th><th>Last Name</th></tr>");
            //Modified HTML table with respective column names of Paper
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("SELECT * FROM Author WHERE author_name LIKE '%"+keyword+"%' or preferred_name LIKE '%"+keyword+"%'");
                        while (rs.next()) {
                            //Declared variables to save results from queries, according to their columns and attributes
                            int id = rs.getInt(1);
                            String author_name = rs.getString(2);
                            String preferred_name = rs.getString(3);
                            String first_name = rs.getString(4);
                            String last_name= rs.getString(5);
         
                        //Print out the record
                        out.println("<tr><td>" + id + "</td><td>" + author_name
                        + "</td><td>" + preferred_name+ "</td><td>"
                        + first_name + "</td><td>" + last_name +  "</td></tr>");
                    }
                    }
                }
                
                else if(param.equals("Paper")){
                    if(missingField){
                        out.println("<li>Search keyword is missing.</li>");
                    }
                    else{

                    out.println("<html>");
                    out.println("<head>");
                    String title = "Search Results for keyword \""+keyword+"\""+ " in Paper:";
                    out.println("<title>" + title + "</title>");
                    out.println("</head>");
                    out.println("<body bgcolor=white>");
                    out.println("<h1>" + title + "</h1>");
                
                        out.println("Received results:<p>");
                        out.println("<p><a href=.././index.html>Back to menu</a></p>");
                        out.println("<table border=\"1\">");
                        out.println("<tr><th>ID</th><th>paper_id</th><th>title_str</th><th>authors_str</th><th>area</th><th>comments_str</th></tr>");
						//Modified HTML table with respective column names of Paper
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("SELECT ID, paper_id, title_str, authors_str, area, comments_str FROM Paper WHERE title_str LIKE '%"+keyword+"%' or authors_str LIKE '%"+keyword+"%'");
                        while (rs.next()) {
                            //Declared variables to save results from queries, according to their columns and attributes
                            int id = rs.getInt(1);
                            int paper_id = rs.getInt(2);
                            String title_str = rs.getString(3);
                            String authors_str= rs.getString(4);
                            String area = rs.getString(5);
                            
                            String comments_str = rs.getString(6);
                
    
                        //Print out the record
                        out.println("<tr><td>" + id + "</td><td>" + paper_id
                        + "</td><td>" + title_str + "</td><td>"
                        + authors_str + "</td><td>" + area +  "</td><td>"
                        + comments_str +  "</td></tr>");
                    }
                }
                    
                }
                
					out.println("</table>");
					
					out.println("</body>");
					out.println("</html>");
			}
			con.close();

		} catch (SQLException ex) {
			out.println("SQLException caught<br>");
			out.println("---<br>");
			while (ex != null) {
				out.println("Message   : " + ex.getMessage() + "<br>");
				out.println("SQLState  : " + ex.getSQLState() + "<br>");
				out.println("ErrorCode : " + ex.getErrorCode() + "<br>");
				out.println("---<br>");
				ex = ex.getNextException();
			}
		}
	}
}
