<#-- @ftlvariable name="progress" type="com.linecorp.bot.model.response.NarrowcastProgressResponse" -->
<#-- @ftlvariable name="audienceGroup" type="com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse" -->
<#-- @ftlvariable name="jobs" type="java.util.List<com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse.Job>" -->
<#import "../../__wrapper.ftlh" as wrapper>
<@wrapper.main>
    <h1>Multicast result</h1>

    <p>Sent message: ${requestId}</p>

    <#include "../__retargeting.ftlh">

</@wrapper.main>
