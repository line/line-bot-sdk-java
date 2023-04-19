<#import "../__wrapper.ftlh" as wrapper>
<@wrapper.main>

    <h1>Webhook testing result</h1>

    <table class="table table-bordered">
        <tr>
            <th>Success</th>
            <td>${response.success!?c}</td>
        </tr>
        <tr>
            <th>timestamp</th>
            <td>${response.timestamp.epochSecond!}</td>
        </tr>
        <tr>
            <th>statusCode</th>
            <td>${response.statusCode}</td>
        </tr>
        <tr>
            <th>Reason</th>
            <td>${response.reason!"-"}</td>
        </tr>
        <tr>
            <th>Detail</th>
            <td>${response.detail!"-"}</td>
        </tr>
    </table>

</@wrapper.main>
