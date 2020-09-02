<#-- @ftlvariable name="audienceGroup" type="com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse" -->
<#-- @ftlvariable name="jobs" type="java.util.List<com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse.Job>" -->
<#import "../__wrapper.ftlh" as wrapper>
<@wrapper.main>
    <h1>Add more audience into ${audienceGroup.description}(ID: ${audienceGroup.audienceGroupId})</h1>

    <form method="post" action="/manage_audience/add_audience_by_file/${audienceGroup.audienceGroupId}"
          enctype="multipart/form-data">
        <div class="form-group">
            <label for="uploadDescription">uploadDescription(Optional)</label>
            <input type="text" class="form-control" id="uploadDescription" name="uploadDescription">
        </div>
        <div class="form-group">
            <label for="audiences">audiences</label>
            <input class="form-control" id="audiences" name="file" type="file" required />
            <small id="toHelp" class="form-text text-muted">UserID list or IFA list split by new line.</small>
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>

</@wrapper.main>
