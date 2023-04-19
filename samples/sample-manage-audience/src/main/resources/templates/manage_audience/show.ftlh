<#-- @ftlvariable name="audienceGroup" type="com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse" -->
<#-- @ftlvariable name="jobs" type="java.util.List<com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse.Job>" -->
<#import "../__wrapper.ftlh" as wrapper>
<@wrapper.main>
<h1>LINE Messaging API Demo App</h1>

    <#if audienceGroup.permission.name() == "READ_WRITE">
        <a href="/manage_audience/update_description/${audienceGroup.audienceGroupId}" class="btn btn-primary">Rename
            audience group</a>
        <a href="/manage_audience/add_audience/${audienceGroup.audienceGroupId}" class="btn btn-primary">Add
            audience for this audience group.</a>
        <a href="/manage_audience/add_audience_by_file/${audienceGroup.audienceGroupId}"
           class="btn btn-primary">Add
            audience for this audience group(By file)</a>
    <#else>
        <div class="alert alert-info" role="alert">
            This audience group is read only.
        </div>
    </#if>

<table class="table table-bordered table-hover">
    <tr>
        <th>audienceGroupId</th>
        <td>${audienceGroup.audienceGroupId?c}</td>
    </tr>
    <tr>
        <th>type</th>
        <td>${audienceGroup.type}</td>
    </tr>
    <tr>
        <th>description</th>
        <td>${audienceGroup.description}</td>
    </tr>
    <tr>
        <th>status</th>
        <td>${audienceGroup.status}</td>
    </tr>
    <tr>
        <th>failed type</th>
        <td>${audienceGroup.failedType!"-"}</td>
    </tr>
    <tr>
        <th>audience count</th>
        <td>${audienceGroup.audienceCount}</td>
    </tr>
    <tr>
        <th>created</th>
        <td>${(audienceGroup.created*1000)?number_to_datetime?iso_local_nz}</td>
    </tr>
    <tr>
        <th>requestId</th>
        <td>${audienceGroup.requestId!"-"}</td>
    </tr>
    <tr>
        <th>clickUrl</th>
        <td>${audienceGroup.clickUrl!"-"}</td>
    </tr>
    <tr>
        <th>permission</th>
        <td>${audienceGroup.permission!"-"}</td>
    </tr>
    <tr>
        <th>create route</th>
        <td>${audienceGroup.createRoute!"-"}</td>
    </tr>
    <tr>
        <th>is ifa audience</th>
        <td>${audienceGroup.isIfaAudience?string('IFA', 'userId')}</td>
    </tr>
</table>

<h2>Jobs</h2>
<table class="table table-hover">
    <tr>
        <th>job id</th>
        <th>description</th>
        <th>type</th>
        <th>status</th>
        <th>failed type</th>
        <th>audience count</th>
        <th>created</th>
    </tr>
    <#list jobs as job>
        <tr>
            <td>${job.audienceGroupJobId?c}</td>
            <td>${job.description!"-"}</td>
            <td>${job.type}</td>
            <td>${job.jobStatus}</td>
            <td>${job.failedType!"-"}</td>
            <td>${job.audienceCount}</td>
            <td>${(job.created*1000)?number_to_datetime?iso_local_nz}</td>
        </tr>
    </#list>
</table>

    <form method="POST" action="/manage_audience/${audienceGroup.audienceGroupId}/delete"
          onsubmit="return confirm('Do you want to delete this audience group? This operation cannot undo.')">
        <input type="submit" value="DELETE" class="btn btn-danger">
    </form>

</@wrapper.main>
