<#-- @ftlvariable name="totalUsage" type="java.lang.Long" -->
<#-- @ftlvariable name="quota" type="com.linecorp.bot.messaging.model.MessageQuotaResponse" -->
<#import "../../__wrapper.ftlh" as wrapper>
<@wrapper.main>

    <h1>The target limit for additional messages</h1>
    <table class="table">
        <tr>
            <th>Type</th>
            <td>${quota.type()}</td>
        </tr>
        <tr>
            <th>Value</th>
            <td>${quota.value()}</td>
        </tr>
    </table>

    <h1>The number of messages sent this month</h1>
    ${totalUsage}
</@wrapper.main>
