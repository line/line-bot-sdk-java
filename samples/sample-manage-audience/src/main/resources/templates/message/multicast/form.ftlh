<#-- @ftlvariable name="audienceGroup" type="com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse" -->
<#-- @ftlvariable name="jobs" type="java.util.List<com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse.Job>" -->
<#import "../../__wrapper.ftlh" as wrapper>
<@wrapper.main>
    <h1>Send push message</h1>

    <form method="post" action="/message/multicast">
        <div class="form-group">
            <label for="to">$.to</label>
            <textarea class="form-control" name="to" id="to" aria-describedby="toHelp" rows="15"></textarea>
            <small id="toHelp" class="form-text text-muted">The destination userID. Split with new line. Max:
                150 user IDs
            </small>
        </div>
        <#include "../__message_form.ftlh">
    </form>

</@wrapper.main>