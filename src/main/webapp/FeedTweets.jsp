<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
<c:when test="${empty tweets}">
  <div class="w3-container w3-card w3-round w3-white w3-section w3-padding w3-center">
    <p class="w3-opacity"><i class="fa fa-info-circle"></i> No posts yet. Follow some users to see their tweets here!</p>
  </div>
</c:when>
<c:otherwise>
<c:forEach var="t" items="${tweets}">
 <div id="${t.id}" class="w3-container w3-card w3-section w3-white w3-round w3-animate-opacity"><br>
   <img src="${t.upicture}" alt="Avatar" class="w3-left w3-circle w3-margin-right" style="width:60px">
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

   <c:if test="${not empty t.comments}">
       <div class="w3-margin-top w3-margin-bottom">
       <c:forEach var="c" items="${t.comments}">
           <div id="${c.id}" class="w3-panel w3-light-grey w3-leftbar w3-border-theme w3-padding" style="margin-left: 20px;">
               <img src="${c.upicture}" alt="Avatar" class="w3-left w3-circle w3-margin-right" style="width:30px">
               <span class="w3-right w3-opacity w3-small"> ${c.postDateTime} </span>
               <h6 style="margin:0; font-weight: bold;"> ${c.uname} </h6>
               <p style="margin-top:5px; font-size: 0.9em;"> ${c.content} </p>
           </div>
       </c:forEach>
       </div>
   </c:if>

   <hr class="w3-clear" style="margin: 10px 0;">
   <div class="w3-row w3-margin-bottom w3-padding-small">
       <div class="w3-col m10 s9">
           <input type="text" class="commentText w3-input w3-border w3-round w3-light-grey" placeholder="Escribe un comentario o respuesta...">
       </div>
       <div class="w3-col m2 s3" style="padding-left:10px;">
           <button type="button" class="commentTweetBtn w3-button w3-theme w3-round" style="width:100%"><i class="fa fa-reply"></i></button>
       </div>
   </div>
 </div>
</c:forEach>
</c:otherwise>
</c:choose>