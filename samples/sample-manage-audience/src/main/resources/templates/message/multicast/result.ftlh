<#-- @ftlvariable name="requestId" type="java.lang.String" -->
<#import "../../__wrapper.ftlh" as wrapper>
<@wrapper.main>
    <h1>Multicast result</h1>

    <p>Sent message: ${requestId}</p>

    <#include "../__retargeting.ftlh">

</@wrapper.main>
