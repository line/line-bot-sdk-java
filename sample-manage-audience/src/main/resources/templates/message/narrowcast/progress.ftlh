<#-- @ftlvariable name="progress" type="com.linecorp.bot.model.response.NarrowcastProgressResponse" -->
<#-- @ftlvariable name="audienceGroup" type="com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse" -->
<#-- @ftlvariable name="jobs" type="java.util.List<com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse.Job>" -->
<#import "../../__wrapper.ftlh" as wrapper>
<@wrapper.main>
    <h1>Narrowcast progress</h1>

    <p>${requestId}</p>

    <#include "../__retargeting.ftlh">

    <table class="table table-bordered">
        <tr>
            <th>Field name</th>
            <th>Response</th>
            <th>Description</th>
        </tr>
        <tr>
            <td>Phase</td>
            <td>${progress.phase}</td>
            <td>The current status.</td>
        </tr>
        <tr>
            <td>successCount</td>
            <td>${progress.successCount!"-"}</td>
            <td>The number of users who successfully received the message.<br>
                (Not available when phase is waiting)
            </td>
        </tr>
        <tr>
            <td>failureCount</td>
            <td>${progress.failureCount!"-"}</td>
            <td>The number of users who failed to receive the message.<br>
                (Not available when phase is waiting)
            </td>
        </tr>
        <tr>
            <td>targetCount</td>
            <td>${progress.targetCount!"-"}</td>
            <td>
                The number of intended recipients of the message.<br>
                (Not available when phase is waiting)
            </td>
        </tr>
        <tr>
            <td>failedDescription</td>
            <td>${progress.failedDescription!"-"}</td>
            <td> The reason why the message failed to be sent. This is only included with a phase property value
                of failed.
            </td>
        </tr>
        <tr>
            <td>failedDescription</td>
            <td>${progress.failedDescription!"-"}</td>
            <td>
                The reason why the message failed to be sent. This is only included with a phase property value
                of failed.
            </td>
        </tr>
        <tr>
            <td>errorCode</td>
            <td>${progress.errorCode!"-"}</td>
            <td>A brief summary of the error.</td>
        </tr>
    </table>

</@wrapper.main>
