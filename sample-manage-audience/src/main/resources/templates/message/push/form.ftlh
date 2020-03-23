<#-- @ftlvariable name="audienceGroup" type="com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse" -->
<#-- @ftlvariable name="jobs" type="java.util.List<com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse.Job>" -->
<#import "../../__wrapper.ftlh" as wrapper>
<@wrapper.main>
    <h1>Send push message</h1>

    <form method="post" action="/message/push">
        <div class="form-group">
            <label for="to">$.to</label>
            <input type="text" class="form-control" name="to" id="to" aria-describedby="toHelp">
            <small id="toHelp" class="form-text text-muted">The destination userID.</small>
        </div>
        <#include "../__message_form.ftlh">
    </form>

</@wrapper.main>
