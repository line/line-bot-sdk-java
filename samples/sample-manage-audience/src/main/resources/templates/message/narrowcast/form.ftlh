<#-- @ftlvariable name="subscriptionPeriods" type="com.linecorp.bot.model.Narrowcast.SubscriptionPeriodDemographicFilter.SubscriptionPeriod[]" -->
<#-- @ftlvariable name="areaCodes" type="com.linecorp.bot.model.Narrowcast.AreaDemographicFilter.AreaCode[]" -->
<#-- @ftlvariable name="appTypes" type="com.linecorp.bot.model.Narrowcast.AppTypeDemographicFilter.AppType[]" -->
<#-- @ftlvariable name="ages" type="com.linecorp.bot.model.Narrowcast.AgeDemographicFilter.Age[]" -->
<#-- @ftlvariable name="audienceGroup" type="com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse" -->
<#-- @ftlvariable name="jobs" type="java.util.List<com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse.Job>" -->
<#import "../../__wrapper.ftlh" as wrapper>
<@wrapper.main>
    <h1>Send narrowcast message</h1>

    <form method="POST" action="/message/narrowcast">
        <div class="form-group clearfix">
            <div class="row">
                <h2>Audience Group</h2>
            </div>
            <div class="row">
                <div class="col-sm-5">
                    <label>
                        <textarea name="recipient"></textarea>
                    </label>

                    <hr>
                    <p>Examples</p>
                    <pre><code>{"type":"audience","audienceGroupId":[AUDIENCE_GROUP_ID]}</code></pre>
                    <pre><code>{"type":"operator","and":[{"audienceGroupId":[AUDIENCE_GROUP_ID]}, {"audienceGroupId":[AUDIENCE_GROUP_ID]}]}</code></pre>
                </div>
                <div class="col-sm-5">
                    <p>Available audience groups</p>
                    <table class="table table-hover">
                        <tr>
                            <th>ID</th>
                            <th>Description</th>
                            <th>Type</th>
                        </tr>
                        <#list audienceGroups as audienceGroup>
                            <tr>
                                <td>${audienceGroup.audienceGroupId}</td>
                                <td>${audienceGroup.description}</td>
                                <td>${audienceGroup.type}</td>
                            </tr>
                        </#list>
                    </table>
                </div>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-2">
                Gender
            </div>
            <div class="col-sm-10">
                <div class="form-check">
                    <input type="radio" class="form-check-input" id="genderAll"
                           name="gender" value="">
                    <label class="form-check-label" for="genderAll">ALL</label>
                </div>
                <div class="form-check">
                    <input type="radio" class="form-check-input" id="genderMale"
                           name="gender" value="MALE">
                    <label class="form-check-label" for="genderMale">MALE</label>
                </div>
                <div class="form-check">
                    <input type="radio" class="form-check-input" id="genderFemale"
                           name="gender" value="FEMALE"
                           checked>
                    <label class="form-check-label" for="genderFemale">FEMALE</label>
                </div>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-2">
                Age
            </div>
            <div class="col-sm-10">
                <div>
                    <label>
                        greater than or equals:
                        <select name="ageGte">
                            <option value=""></option>
                            <#list ages as age>
                                <option value="${age}">${age}</option>
                            </#list>
                        </select>
                    </label>
                </div>
                <div>
                    <label>
                        less than:
                        <select name="ageLt">
                            <option value=""></option>
                            <#list ages as age>
                                <option value="${age}">${age}</option>
                            </#list>
                        </select>
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-2">
                App Type(OS)
            </div>
            <div class="col-sm-10">
                <label>
                    <select name="appType">
                        <option value=""></option>
                        <#list appTypes as appType>
                            <option value="${appType}">${appType}</option>
                        </#list>
                    </select>
                </label>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-2">
                Area
            </div>
            <div class="col-sm-10">
                <#list areaCodes as areaCode>
                    <label>
                        <input type="checkbox" name="areaCode" value="${areaCode}"> ${areaCode}
                    </label>
                </#list>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-2">
                subscription period
            </div>
            <div class="col-sm-10">
                <label>
                    subscriptionPeriod greater than or equals:
                    <select name="subscriptionPeriodGte">
                        <option value=""></option>
                        <#list subscriptionPeriods as subscriptionPeriod>
                            <option value="${subscriptionPeriod}">${subscriptionPeriod}</option>
                        </#list>
                    </select>
                </label>
                <label>
                    subscriptionPeriod less than:
                    <select name="subscriptionPeriodLt">
                        <option value=""></option>
                        <#list subscriptionPeriods as subscriptionPeriod>
                            <option value="${subscriptionPeriod}">${subscriptionPeriod}</option>
                        </#list>
                    </select>
                </label>
            </div>
        </div>

        <div class="form-group row">
            <div class="col-sm-2">
                <label for="maxLimit">Max Limit</label>
            </div>
            <div class="col-sm-10">
                <input class="form-control" name="maxLimit" id="maxLimit"
                       aria-describedby="maxLimitHelp" type="number" min="0" step="1">
                <small id="maxLimitHelp" class="form-text text-muted">The maximum number of narrowcast messages
                    to send..</small>
            </div>
        </div>
        <#include "../__message_form.ftlh">
    </form>

</@wrapper.main>
