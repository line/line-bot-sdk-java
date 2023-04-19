<#-- @ftlvariable name="audienceGroups" type="com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse" -->
<#import "../__wrapper.ftlh" as wrapper>
<@wrapper.main>

    <h1>Create audience for impression-based retargeting</h1>

    <form method="post" action="/manage_audience/imp">
        <div class="form-group">
            <label for="description">description</label>
            <input type="text" class="form-control" id="description" name="description" required>
        </div>
        <div class="form-group">
            <label for="requestId">request ID</label>
            <input type="text" class="form-control" id="requestId" name="requestId" value="${requestId!''}" required>
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>

</@wrapper.main>
