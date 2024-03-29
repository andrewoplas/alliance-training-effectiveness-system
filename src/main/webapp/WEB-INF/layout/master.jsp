<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles-extras" prefix="tilesx" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    
		<title><tiles:getAsString name="title" /></title>
			
		<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
	    <link type="text/css" rel="stylesheet" href="/plugins/bootstrap/dist/css/bootstrap.min.css">
	    <link type="text/css" rel="stylesheet" href="/plugins/sidebar-nav/dist/sidebar-nav.min.css">
	    
	    <tiles:importAttribute name="stylesheets" />
	    <c:forEach var="css" items="${stylesheets}">
		    <link type="text/css" rel="stylesheet" href="<c:out value='${css}' /> " />
		</c:forEach>
	    
	    <link type="text/css" rel="stylesheet" href="/css/animate.css">
	    <link type="text/css" rel="stylesheet" href="/css/style.css">
	    <link type="text/css" rel="stylesheet" href="/css/default.css">
    
	    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	    <!--[if lt IE 9]>
	    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
	</head>

	<body class="fix-header">
	
		<tiles:insertAttribute name="header" />   	    
        <tiles:insertAttribute name="sidebar" />
        <tiles:insertAttribute name="body" />
        <tiles:insertAttribute name="footer" />	    
	    
	    <script type="text/javascript" src="/plugins/jquery/dist/jquery.min.js"></script>
	    <script type="text/javascript" src="/plugins/bootstrap/js/popper.min.js"></script>
	    <script type="text/javascript" src="/plugins/bootstrap/dist/js/bootstrap.min.js"></script>
	    <script type="text/javascript" src="/plugins/sidebar-nav/dist/sidebar-nav.min.js"></script>
	    <script type="text/javascript" src="/js/jquery.slimscroll.js"></script>
	    <script type="text/javascript" src="/js/waves.js"></script>
	    <script type="text/javascript" src="/js/custom.min.js"></script>
	    	    	    
		<tiles:importAttribute name="javascripts" />
	    <c:forEach var="script" items="${javascripts}">
		    <script type="text/javascript" src="<c:out value='${script}' />"></script>
		</c:forEach>
		
		<c:if test="${map == true}">
			<script type="text/javascript" src="/plugins/google/map/map.js"></script>
			<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDz1soD04ng1I-BomUp18X3XceFl4Bw7_k&libraries=places&callback=initAutocomplete"
         	async defer></script>
         </c:if>
         
	    <script src="/plugins/styleswitcher/jQuery.style.switcher.js"></script>	
	</body>
</html>