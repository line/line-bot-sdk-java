<#-- @ftlvariable name="audienceGroups" type="com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse" -->
<#import "../__wrapper.ftlh" as wrapper>
<@wrapper.main>

    <h1>LINE Messaging API Demo App</h1>

    <a href="/manage_audience/upload" class="btn btn-primary">Upload new audience group</a>
    <a href="/manage_audience/upload_by_file" class="btn btn-primary">Upload new audience group(by file)</a>
    <a href="/manage_audience/imp" class="btn btn-primary">Create new imp based audience</a>
    <a href="/manage_audience/click" class="btn btn-primary">Create new click based audience</a>

    <table class="table table-bordered">
        <tr>
            <th>audienceGroupId</th>
            <th>type</th>
            <th>description</th>
            <th>status</th>
            <th>failed type</th>
            <th>audience count</th>
            <th>created</th>
            <th>requestId</th>
            <th>clickUrl</th>
            <th>permission</th>
            <th>create route</th>
            <th>is ifa audience</th>
        </tr>
        <#list response.audienceGroups as audienceGroup>
            <tr>
                <td>
                    <a href="/manage_audience/${audienceGroup.audienceGroupId?c}">${audienceGroup.audienceGroupId?c}</a>
                </td>
                <td>${audienceGroup.type}</td>
                <td>${audienceGroup.description}</td>
                <td style="<#if audienceGroup.status="EXPIRED">color: red</#if>">${audienceGroup.status}</td>
                <td>${audienceGroup.failedType!"-"}</td>
                <td>${audienceGroup.audienceCount}</td>
                <td>${(audienceGroup.created*1000)?number_to_datetime?iso_local_nz}</td>
                <td>${audienceGroup.requestId!"-"}</td>
                <td>${audienceGroup.clickUrl!"-"}</td>
                <td>${audienceGroup.permission!"-"}</td>
                <td>${audienceGroup.createRoute!"-"}</td>
                <td>${audienceGroup.isIfaAudience?string('IFA', 'userId')}</td>
            </tr>
        </#list>
    </table>

    <p>current page: ${response.page}</p>
    <p>
        <#if (response.page > 1)>
            <a href="/manage_audience/?page=${response.page-1}">←← </a>
        </#if>
        <#if response.hasNextPage>
            <a href="/manage_audience/?page=${response.page+1}"> →→</a>
        </#if>
    </p>
</@wrapper.main>
