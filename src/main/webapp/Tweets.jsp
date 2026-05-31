<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:forEach var="t" items="${tweets}">       
 <div id="${t.id}" class="w3-container w3-card w3-section w3-white w3-round w3-animate-opacity"><br>
   <img src="${user.picture}" alt="Avatar" class="w3-left w3-circle w3-margin-right" style="width:60px">
   <span class="w3-right w3-opacity"> ${t.postDateTime} </span>
   <h4> ${t.uname} </h4><br>
   <hr class="w3-clear">
   <p> ${t.content} </p>
   <c:choose>
     <c:when test="${t.liked}">
       <button type="button" class="likeTweet w3-button w3-deep-orange w3-margin-bottom w3-round"><i class="fa fa-thumbs-down"></i> &nbsp;Unlike (${t.likeCount})</button>
     </c:when>
     <c:otherwise>
       <button type="button" class="likeTweet w3-button w3-theme w3-margin-bottom w3-round"><i class="fa fa-thumbs-up"></i> &nbsp;Like (${t.likeCount})</button>
     </c:otherwise>
   </c:choose>
   <button type="button" class="delTweet w3-button w3-red w3-margin-bottom w3-round"><i class="fa fa-trash"></i> &nbsp;Delete</button> 
 </div>
</c:forEach>
