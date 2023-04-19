<#list [1,2,3,4,5] as msgno>
    <div class="form-group">
        <label for="message${msgno}">$.message[${msgno}]</label>
        <textarea class="form-control" id="message${msgno}" name="messages" rows="7">{
            "type":"text",
            "text":"Hello, world ${msgno}"
        }</textarea>
        <small id="toHelp" class="form-text text-muted">Message in JSON</small>
    </div>
</#list>
<div class="form-group">
    <div class="form-check">
        <input type="radio" class="form-check-input" id="notificationDisabledTrue"
               name="notificationDisabled" value="true">
        <label class="form-check-label" for="notificationDisabledTrue">Notification Disabled</label>
    </div>
    <div class="form-check">
        <input type="radio" class="form-check-input" id="notificationDisabledFalse"
               name="notificationDisabled" value="false"
               checked>
        <label class="form-check-label" for="notificationDisabledFalse">Notification Enabled</label>
    </div>
</div>
<button type="submit" class="btn btn-primary">Submit</button>
