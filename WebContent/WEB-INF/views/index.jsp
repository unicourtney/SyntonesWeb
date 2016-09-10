<%@page import="com.blackparty.syntones.model.Song"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="java.util.List"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		List<String> lyricsList = (List<String>) session.getAttribute("lyrics");
		Song song = (Song) session.getAttribute("song");
		if (song != null) {
			String artistName = song.getArtistName();
			String songTitle = song.getSongTitle();
		}
	%>
	<p>you are at the index page.</p>
	<p>${message}</p>

	<form method="POST" enctype="multipart/form-data" action="upload">
		File to upload: <input type="file" name="file" /><br> <input
			type="text" name="artistName" value="${artistName} " /> <input
			type="text" name="songTitle" value="${songTitle}" /> <input
			type="submit" name="action" value="read" /> <input type="submit"
			name="action" value="save" />
	</form>
	<a href="songList"><button>Show list of songs in the database</button></a>
	<audio controls> 
			<source src="D:/Our_Files1/Eric/School/Thesis/Syntones/Songs/Uploaded/50450/500700.mp3" type="audio/mpeg">
	</audio>
	<a href="play"><button>play</button></a>

	<%
		if (lyricsList != null) {
			for (int i = 0; i < lyricsList.size(); i++) {
				String output = "<p>" + lyricsList.get(i) + "</p>";
				out.print(output);
				/* if(lyricsList.get(i).equals(",")&&lyricsList.get(i-1).equals(",")){
					out.println("<br>");
				}else{
					out.println(lyricsList.get(i));
				}	 */
			}
		}
	%>


</body>
</html>