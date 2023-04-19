<#-- @ftlvariable name="audienceGroup" type="com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse" -->
<#-- @ftlvariable name="jobs" type="java.util.List<com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse.Job>" -->
<#import "../../__wrapper.ftlh" as wrapper>
<@wrapper.main>
    <h1>Send broadcast message</h1>

    <form method="post" action="/message/broadcast">
        <#include "../__message_form.ftlh">
    </form>

</@wrapper.main>