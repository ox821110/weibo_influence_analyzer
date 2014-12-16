<%@page import="weibo4j.model.Status"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <link href="default.css" rel="stylesheet"> 
 
    <link href="bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="bootstrap-responsive.css" rel="stylesheet">
    <link href="bootstrap.css" rel="stylesheet">

    <style type="text/css">
	body {
		padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
	}
	
	/* Custom container */
	.container-narrow {
		margin: 0 auto;
		max-width: 900px;
		border-style: solid;
		border-color: transparent;
		background-color: #D8D8D8	;
		z-index: 9;
		height : 100%;
		-moz-border-radius: 15px;
		border-radius: 15px;
	}
	
	.container-narrow > hr {
		margin: 30px 0;
	}
	
	.sidebar-nav {
		padding: 20px 0;
	}
	
	@media (max-width: 980px) {
		/* Enable use of floated navbar text */
		.navbar-text.pull-right {
			float: none;
			padding-left: 5px;
			padding-right: 5px;
		}
	}	 
	</style>  


  </head>

  <body>
  <!-- <img src="http://www.logomaker.com/logo-images/555b7084659959ad.gif"/>
  <a href="http://www.logomaker.com"><img src="http://www.logomaker.com/images/logos.gif" alt="logo design" border="0"/></a> -->

  
  <script src="/bootstrap.min.js"></script>
  <script src="/bootstrap.js"></script>
  <script src="/bootstrap-tooltip.js"></script>

  <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          
          <a class="brand pull-left" href="../"><em>软通动力微博影响力分析</em></a>
	  
          <div class="nav-collapse collapse">
			<a href="./" type="button" class="btn btn-primary">« 重新分析</a>
          </div>  <!--  /.nav-collapse  -->
        </div>
      </div>
  </div>  <!-- end of div for nav bar-->


<div class="container-fluid">
  <div class="row-fluid">

<div class="span4">
  <h1>影响力评分结果: <%= request.getAttribute("totalscore") %>/200</h1>
  <table class="table table-condensed">
  <tr>
  <td>微博名称:</td>
  <td><%= request.getAttribute("t_name") %></td>
  </tr>
  <tr>
  <td>微博粉丝数: </td>
  <td><%= request.getAttribute("fcount") %></td>
  </tr>
  <tr>
  <td>粉丝评分:</td>
  <td><%= request.getAttribute("fscore") %></td>
  </tr>
  <tr>
  <td>被转发数:</td>
  <td><%= request.getAttribute("rtcount") %></td>
  </tr>
  <tr>
  <td>被转发评分:</td>
  <td><%= request.getAttribute("rtscore") %></td>
  </tr>
  <tr>
  <td>最近提到:</td>
  <td><%= request.getAttribute("mcount") %>/100</td>
  </tr>
  </table>
  <br>
  <form action="SaveData" method="post">
  <input type="hidden" name="totalscore" value="<%= request.getAttribute("totalscore") %>">
  <input type="hidden" name="t_name" value="<%= request.getAttribute("t_name") %>">
  <input type="hidden" name="fcount" value="<%= request.getAttribute("fcount") %>">
  <input type="hidden" name="fscore" value="<%= request.getAttribute("fscore") %>">
  <input type="hidden" name="rtcount" value="<%= request.getAttribute("rtcount") %>">
  <input type="hidden" name="rtscore" value="<%= request.getAttribute("rtscore") %>">
  <input type="hidden" name="mcount" value="<%= request.getAttribute("mcount") %>">
  <input type="submit" value="保存到数据库" class="btn btn-success">
  <a class="btn btn-primary" href="DisplayAll">查看数据库</a>
  </form>
</div>

<div class="span8">
  <div class="row-fluid">
  <div>
  <h3>最新2条微博:</h3>
  	<table class="table table-condensed"> 
  	<tr>
  		<td><b>微博内容</b></td>
  		<td><b>被转发数</b></td>
  	</tr>
		<%
		java.util.List<Status> rtweets  = (java.util.List<Status>) request.getAttribute("rtweets");
		int count = 0 ;
		for ( Status tweet : rtweets) {
			if (count >=2) break;
			int retweetCount = (int)tweet.getRepostsCount();
			String tweetText = tweet.getText();
			String mpic = tweet.getBmiddlePic();
			count ++;
		%>
		<tr>
			<td><%= tweetText %></td>
			<td><b><%= retweetCount%></b></td>
		</tr>
		<tr>
			<td><img src="<%= mpic%>"></td>
		</tr>
		<%	
		}
		%>
  	</table>
  </div> <!-- end of the span6 for table-->
  </div> <!-- end of row-fluid for span6 -->


<div class="row-fluid">
  <div>
  <h3>粉丝分布: </h3>
  <div><img src="pic.jpg"></div> 
  </div> <!-- end of span6 for map-canvas-->
</div> <!-- end of row fluid for span6-->
 
</div> <!-- end of the span8 div -->


</div>  <!-- end the div row-fluid -->
</div>  <!-- ends the div container-fluid --> 

</body>
  </html>