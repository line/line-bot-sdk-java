<#-- @ftlvariable name="audienceGroups" type="com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse" -->
<#import "../__wrapper.ftlh" as wrapper>
<@wrapper.main>

    <h1>Update description: ${audienceGroup.description}(ID: ${audienceGroup.audienceGroupId})</h1>

    <form method="post" action="/manage_audience/update_description/${audienceGroup.audienceGroupId}">
        <div class="form-group">
            <label for="description">description</label>
            <input type="text" class="form-control" id="description" name="description"
                   value="${audienceGroup.description}" required>
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>

</@wrapper.main>
