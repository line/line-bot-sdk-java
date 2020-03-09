<#-- @ftlvariable name="audienceGroups" type="com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse" -->
<#import "../__wrapper.ftlh" as wrapper>
<@wrapper.main>

    <h1>Upload new audience group from userId or MID list</h1>

    <form method="post" action="/manage_audience/upload">
        <div class="form-group">
            <label for="description">description</label>
            <input type="text" class="form-control" id="description" name="description" required>
        </div>
        <div class="form-group">
            <label for="uploadDescription">uploadDescription(Optional)</label>
            <input type="text" class="form-control" id="uploadDescription" name="uploadDescription">
        </div>
        <div class="form-group">
            <div class="form-check">
                <input type="radio" class="form-check-input" id="isIfaAudienceFalse"
                       name="isIfaAudience" value="false" checked>
                <label class="form-check-label" for="isIfaAudienceFalse">userId</label>
            </div>
            <div class="form-check">
                <input type="radio" class="form-check-input" id="isIfaAudienceTrue"
                       name="isIfaAudience" value="true">
                <label class="form-check-label" for="isIfaAudienceTrue">IFA</label>
            </div>
        </div>
        <div class="form-group">
            <label for="audiences">audiences</label>
            <textarea class="form-control" id="audiences" name="audiences" rows="7" required></textarea>
            <small id="toHelp" class="form-text text-muted">UserID list or IFA list split by new line.</small>
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>

</@wrapper.main>
