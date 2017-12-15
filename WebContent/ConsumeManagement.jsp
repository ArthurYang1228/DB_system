<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.Connector, models.Consume, models.ConsumeDAO, 
	models.Product, models.ProductDAO, models.Inventory, models.InventoryDAO, java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>消耗管理</title>
<%@ include  file="InHead.html" %>
</head>
<body>
	<%
		request.setCharacterEncoding("UTF-8");
		ConsumeDAO consumeDAO = new ConsumeDAO(Connector.getConn());
		ProductDAO productDAO = new ProductDAO(Connector.getConn());
		InventoryDAO inventoryDAO = new InventoryDAO(Connector.getConn());
	%>
	<%@ include file="Header.html" %>
	<!-- DELTE FORM ------------------------------------------------------------------------------------------------>
	
	<%
		String deletedId = request.getParameter("deletedId");
		if(deletedId != null) {
			consumeDAO.delete(consumeDAO.getConsume(Integer.parseInt(deletedId)));
		}
	%>
	
	
	
	<!-- INSERT FORM ------------------------------------------------------------------------------------------------>
	
	<h2> 新增消耗 </h2>
	<br />
	<form class="form-inline" action="ConsumeManagement.jsp" method="POST">
		產品：<select class="form-control" name="pId" >
	<%
			for(Product p : productDAO.getAllProducts()) {
	%>
				<option value=<%=p.id %>><%=p.name +  "(" + p.size + ")" %></option>
	<%
			}
	%>
			</select>
		存貨： <select  class="form-control" name="iId" >
	<%
			for(Inventory i : inventoryDAO.getAllInventorys()) {
	%>
				<option value=<%=i.id %>><%=i.name %></option>
	<%
			}
	%>
			</select>
		<div class="form-group">
		   <label for="quantity">數量： </label>
		   <input type="number" class="form-control" id="quantity" name="quantity" required> &nbsp;
		</div>
		<div class="form-group">
		   <input type="submit" value="新增">
		 </div>
	</form>
	<br />
	
	<% 
		String name = request.getParameter("quantity");
		String stringPId = request.getParameter("pId");
		String stringIId = request.getParameter("iId");
		int pId = 0, iId = 0, quantity = 0;
		
		if(stringIId != null)
			iId = Integer.parseInt(stringIId);
		if(stringPId != null)
			pId = Integer.parseInt(stringPId);
		if(name != null)
			quantity = Integer.parseInt(name);
		
		if(name != null && stringPId != null && stringIId != null) {
			if(consumeDAO.inConsume(iId, pId)){
				out.println("已存在無法新增");
				
			}
			else{
			consumeDAO.insert(new Consume(quantity, productDAO.getProduct(pId), 
					inventoryDAO.getInventory(iId)));
			out.println("新增消耗成功");
			}
		}
			
   			else if(name != null || stringPId != null || stringIId != null) { 
			out.println("請填入完整的資料");
		} 
	%>
	
	<!-- UPDATE FORM ------------------------------------------------------------------------------------------------>
		<%
		// 表單參數
		String updatedIdfromForm = request.getParameter("updatedIdfromForm");
		String updatedQuantity = request.getParameter("updatedQuantity");
		String updatedPId = request.getParameter("updatedPid");
		String updatedIId = request.getParameter("updatedIid");
		int upPId = 0, upIId = 0, upQuantity = 0;
		
		if(updatedQuantity != null)
			upQuantity = Integer.parseInt(updatedQuantity);
		if(updatedPId != null)
			upPId = Integer.parseInt(updatedPId);
		if(updatedIId != null)
			upIId = Integer.parseInt(updatedIId);
		
		if(updatedIdfromForm != null && updatedPId != null && updatedIId != null)
			consumeDAO.update(
				new Consume(
					Integer.parseInt(updatedIdfromForm), upQuantity, 
					productDAO.getProduct(upPId), inventoryDAO.getInventory(upIId)
				)
			);
		
	    %>
		
		
	
	
	<%	
		// 網址參數
		
		String updatedId = request.getParameter("updatedId");
		
		if(updatedId != null && updatedIdfromForm == null) {
			int id = Integer.parseInt(updatedId);
			Consume c = consumeDAO.getConsume(id);
	%>
			<hr>
			<h3> 修改消耗 </h3>
			<form class="form-inline" action="ConsumeManagement.jsp" method="POST">
				<div class="form-group">
					<label>消耗編號：<e><%= c.id %></e> &nbsp;</label>
					<input type="number" name="updatedIdfromForm" value=<%= c.id %>
					style="display: None;">
				</div>
				
				產品： <select class="form-control" name="updatedPid" >
	<%
			for(Product p : productDAO.getAllProducts()) {
	%>
				<option value=<%=p.id %>><%=p.name %></option>
	<%
			}
	%>
				</select>
		存貨： <select class="form-control" name="updatedIid" >
	<%
			for(Inventory i : inventoryDAO.getAllInventorys()) {
	%>
				<option value=<%=i.id %>><%=i.name %></option>
	<%
			}
	%>
			</select>
				<div class="form-group">
				   <label for="updatedQuantity">數量： </label> 
				   <input type="number" class="form-control" value=<%= c.quantity %>
				   id="updatedQuantity" name="updatedQuantity" required> &nbsp;
				</div>
				 <div class="form-group">
				   <input type="submit" value="確認修改">
				 </div>
			</form>
	<%
		}
	%>
	
	
	
	<table class="table">
	<tr>
		<th> 消耗編號 </td>
		<td> 產品  </td>
		<td> 庫存 </td>
		<td> 數量 </td>
	</tr>
	<% 
		ArrayList<Consume> consumes = consumeDAO.getAllConsumes();
		for(Consume c : consumes) {
	%>
		<tr>
			<td><%= c.id %></td>
			<td><%= c.product.name + "(" + c.product.size + ")"%></td>
			<td><%= c.inventory.name %></td>
			<td><%= c.quantity %></td>
			
			<td>
			<a href=<%= "ConsumeManagement.jsp?updatedId=" + c.id %>>點我修改</a>
				</td>
				
			<td>
			<a href=<%= "ConsumeManagement.jsp?deletedId=" + c.id %>>點我刪除</a>
				</td>
		</tr>
	<%
		} 
	%>
	</table>
	
	<br />
	<br />
	<br />
</body>
</html>