	<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
	
	<div id="page-wrapper">
          <div class="container-fluid">
              <div class="row bg-title">
                  <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
                      <h4 class="page-title">User List</h4> </div>
                  <div class="col-lg-9 col-sm-8 col-md-8 col-xs-12">
                      <ol class="breadcrumb">
                          <li><a href="/ates/dashboard">Dashboard</a></li>
                          <li class="active">Users List</li>
                      </ol>
                  </div>
                  <!-- /.col-lg-12 -->
              </div>
              
              <!-- /row -->
              <div class="row">
	              <div class="col-xs-12">
	              
	              	<div class="panel panel-info">
						<div class="panel-heading">
							<span>Users and Information</span>
						</div>
                            
						<div class="panel-wrapper collapse in" aria-expanded="true">
							<div class="panel-body">
								<div class="table-responsive">
		                            <label class="form-inline">Show
		                                <select id="show-entries" class="form-control input-sm">
		                                    <option value="10">10</option>
		                                    <option value="25">25</option>
		                                    <option value="50">50</option>
		                                    <option value="100">100</option>
		                                </select> entries 
									</label>
		                                
		                           	
		                           	<label class="pull-right">
		                           		Search:
		                           		<input type="search" id="table-search" class="m-l-5">
									</label>
		                             
		                                
		                            <table id="all-users-table" class="table m-b-0 toggle-arrow-tiny" data-page-size="10"
		                            		data-filter="#table-search" data-filter-minimum="1">
		                                <thead>
		                                    <tr>
		                                        <th data-toggle="true">Name</th>
		                                        <th>Position</th>
		                                        <th>Email</th>
		                                        <th>Action</th>
		                                        <th data-hide="all"> Trainings </th>
		                                    </tr>
		                                </thead>
		                                <tbody>
		                                	<c:forEach var="user" items="${users}">
		                                		<tr data-id="${user.id}">
			                                        <td width="25%" class="name">${user.name}</td>
			                                        <td width="25%">${user.position}</td>
			                                        <td width="25%">${user.email}</td>
			                                        <td width="25%">
			                                        	
			                                        	<a href="/ates/users/view/${user.id}">
				                                        	<button type="button" class="btn-edit btn btn-success btn-outline btn-circle m-r-5 p-t-0 p-b-0" data-toggle="tooltip" title="View User" data-placement="top">
				                                        		<i class="mdi mdi-account"></i>
			                                        		</button>
		                                        		</a>
			                                        	
			                                        	<a href="/ates/users/edit/${user.id}">
				                                        	<button type="button" class="btn-edit btn btn-info btn-outline btn-circle m-r-5 p-t-0 p-b-0" data-toggle="tooltip" title="Edit" data-placement="top">
				                                        		<i class="mdi mdi-account-edit"></i>
			                                        		</button>
		                                        		</a>
		                                        		
		                                        		<c:if test="${ user.isAdmin == 0 }">
														<button type="button" class="btn-remove btn btn-danger btn-outline btn-circle p-t-0 p-b-0" data-toggle="tooltip" title="Delete" data-placement="top">
															<i class="mdi mdi-delete"></i>
														</button>
														</c:if>
			                                        </td>
			                                        <td>
			                                        	<ul class="list-group m-b-10">
			                                        		<c:set var="length" value="${user.training.size()}"/>
			                                        		
			                                        		<c:if test="${length != 0}">
				                                        		<c:forEach var = "i" begin = "0" end = "${length-1}">
					                                        		<c:if test="${i < 3}">
																		<li class="list-group-item">
																			${user.training.get(i)}
																			
																			<span class="label label-table ${user.role.get(i)} m-l-5">
																				${user.role.get(i)}
																			</span>
																		</li>
																	</c:if>
																</c:forEach>
															</c:if>
															
															<c:if test="${length == 0}">
																<li class="list-group-item">
																	NO TRAININGS ATTENDED YET
																</li>
															</c:if>
														</ul>                                        
			                                        </td>
			                                    </tr>
		                                	</c:forEach>                                    
		                                </tbody>
		                                <tfoot>
		                                    <tr>
		                                        <td colspan="5">
		                                            <div class="text-right">
		                                                <ul class="pagination pagination-split m-t-30"> </ul>
		                                            </div>
		                                        </td>
		                                    </tr>
		                                </tfoot>
		                            </table>
			                   </div>
                            </div>
						</div>
					</div>	              
	              
				</div>
	  		</div>
		</div>
	</div>
	
	