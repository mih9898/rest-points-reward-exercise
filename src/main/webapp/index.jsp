<html>
<body>
<h2>Fetch Rewards Coding Exercise - Backend Software Engineering</h2>
<h3>Problem</h3>
<p><Our users have points in their accounts. Users only see a single balance in their accounts.
But for reporting purposes we actually track their points per payer/partner. In our system, each transaction record contains:
<i>payer (string), points (integer), date (type timestamp).</i>
    </p>

<p>For earning points it is easy to assign a payer, we know which actions earned the points.
And thus which partner should be paying for the points. <br>
When a user spends points, they don't know or care which payer the points come from.
But, our accounting team does care how the points are spent. There are two rules for determining what points to "spend" first:
</p>
<ul>
<li>  We want the oldest points to be spent first (oldest based on transaction date, not the order they’re received)</li>
<li>  We want no payer's points to go negative.</li>
</ul>
<h3>Solution</h3>
<p>Restful web service</p>

<h4> Endpoints</h4>
<ul>
    <li>GET  at .../rest/payers  - provide all payers information.</li>
    <ul>
        <li>Response: [{ "payer": string>, "balance": integer> },...]</li>
    </ul>

    <li>GET  at .../rest/spends  - will spend points from the OLDEST transactions based on date(field)</li>
    <p>query parameter <code>points</code> IS REQUIRED</p>
    <ul>
        <li>Response:  [{ "payer": string>, "points": integer> },...]</li>
    </ul>

    <li>POST  at .../rest/transaction  - add new transaction</li>
    <p>query parameter <code>points</code> IS REQUIRED</p>
    <ul>
        <li>Response: 1 or 0 that tells was it saved or not</li>
    </ul>
</ul>


        <h5> Example</h5>
        1. Add transactions at with body: <br>
        <code>
            { "payer": "MILLER COORS", "points": 100, "date": "2020-12-02T14:00:00Z" }
            { "payer": "DANNON", "points": 1000, "date": "2020-11-02T14:00:00Z" }
            { "payer": "UNILEVER", "points": 200, "date": "2020-10-31T11:00:00Z" }
        </code><br>
        2. Spend points  at .../rest/spends?points=1200  with folloing response: <br>
        <code>
            [
            { "payer": "UNILEVER", "points": -1000 },
            { "payer": "DANNON", "points": -200 },
            ]
        </code> <br>

        3. Get payers balance response: <br>
        <code>
            {
            "DANNON": 0,
            "UNILEVER": 0,
            "MILLER COORS": 100
            }

        </code> <br>
</body>
</html>
