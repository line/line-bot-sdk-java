<#-- @ftlvariable name="totalUsage" type="java.lang.Long" -->
<#import "../../__wrapper.ftlh" as wrapper>
<@wrapper.main>

    <h1>Get number of sent messages: ${date}</h1>

    <form method="get" action="/message/report/sent">
        <input type="date" required pattern="\d{4}\d{2}\d{2}" name="date" value="${date}">
        <button type="submit">View</button>
    </form>

    <table class="table">
        <tr>
            <th>Type</th>
            <th>Status</th>
            <th>Success</th>
        </tr>
        <tr>
            <td>Broadcast</td>
            <td>${broadcast.status}</td>
            <td>${broadcast.success}</td>
        </tr>
        <tr>
            <td>Multicast</td>
            <td>${multicast.status}</td>
            <td>${multicast.success}</td>
        </tr>
        <tr>
            <td>Push</td>
            <td>${push.status}</td>
            <td>${push.success}</td>
        </tr>
        <tr>
            <td>Reply</td>
            <td>${reply.status}</td>
            <td>${reply.success}</td>
        </tr>
    </table>
</@wrapper.main>
