package com.hedera.services.grpc.controllers;

/*-
 * ‌
 * Hedera Services Node
 * ​
 * Copyright (C) 2018 - 2020 Hedera Hashgraph, LLC
 * ​
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ‍
 */

import com.hedera.services.queries.answering.QueryResponseHelper;
import com.hedera.services.queries.contract.ContractAnswers;
import com.hedera.services.txns.submission.TxnResponseHelper;
import com.hederahashgraph.api.proto.java.HederaFunctionality;
import com.hederahashgraph.api.proto.java.Query;
import com.hederahashgraph.api.proto.java.Response;
import com.hederahashgraph.api.proto.java.Transaction;
import com.hederahashgraph.api.proto.java.TransactionResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.hederahashgraph.api.proto.java.HederaFunctionality.ContractCall;
import static com.hederahashgraph.api.proto.java.HederaFunctionality.ContractCallLocal;
import static com.hederahashgraph.api.proto.java.HederaFunctionality.ContractCreate;
import static com.hederahashgraph.api.proto.java.HederaFunctionality.ContractDelete;
import static com.hederahashgraph.api.proto.java.HederaFunctionality.ContractGetInfo;
import static com.hederahashgraph.api.proto.java.HederaFunctionality.ContractGetRecords;
import static com.hederahashgraph.api.proto.java.HederaFunctionality.ContractUpdate;
import static com.hederahashgraph.api.proto.java.HederaFunctionality.GetBySolidityID;
import static com.hederahashgraph.api.proto.java.HederaFunctionality.SystemDelete;
import static com.hederahashgraph.api.proto.java.HederaFunctionality.SystemUndelete;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.verify;

class ContractControllerTest {
	Query query = Query.getDefaultInstance();
	Transaction txn = Transaction.getDefaultInstance();
	ContractAnswers contractAnswers;
	TxnResponseHelper txnResponseHelper;
	QueryResponseHelper queryResponseHelper;
	StreamObserver<Response> queryObserver;
	StreamObserver<TransactionResponse> txnObserver;

	ContractController subject;

	@BeforeEach
	private void setup() {
		txnObserver = mock(StreamObserver.class);
		queryObserver = mock(StreamObserver.class);

		contractAnswers = mock(ContractAnswers.class);
		txnResponseHelper = mock(TxnResponseHelper.class);
		queryResponseHelper = mock(QueryResponseHelper.class);

		subject = new ContractController(contractAnswers, txnResponseHelper, queryResponseHelper);
	}

	@Test
	public void forwardsCreate() {
		// when:
		subject.createContract(txn, txnObserver);

		// expect:
		verify(txnResponseHelper).submit(txn, txnObserver, ContractCreate);
	}

	@Test
	public void forwardsUpdate() {
		// when:
		subject.updateContract(txn, txnObserver);

		// expect:
		verify(txnResponseHelper).submit(txn, txnObserver, ContractUpdate);
	}

	@Test
	public void forwardsCall() {
		// when:
		subject.contractCallMethod(txn, txnObserver);

		// expect:
		verify(txnResponseHelper).submit(txn, txnObserver, ContractCall);
	}

	@Test
	public void forwardsGetInfo() {
		// when:
		subject.getContractInfo(query, queryObserver);

		// expect:
		verify(contractAnswers).getContractInfo();
		verify(queryResponseHelper).answer(query, queryObserver, null, ContractGetInfo);
	}

	@Test
	public void forwardsLocalCall() {
		// when:
		subject.contractCallLocalMethod(query, queryObserver);

		// expect:
		verify(contractAnswers).contractCallLocal();
		verify(queryResponseHelper).answer(query, queryObserver, null, ContractCallLocal);
	}

	@Test
	public void forwardsGetBytecode() {
		// when:
		subject.contractGetBytecode(query, queryObserver);

		// expect:
		verify(contractAnswers).getBytecode();
		verify(queryResponseHelper).answer(query, queryObserver, null, HederaFunctionality.ContractGetBytecode);
	}

	@Test
	public void forwardsGetBySolidity() {
		// when:
		subject.getBySolidityID(query, queryObserver);

		// expect:
		verify(contractAnswers).getBySolidityId();
		verify(queryResponseHelper).answer(query, queryObserver, null, GetBySolidityID);
	}

	@Test
	public void forwardsGetRecord() {
		// when:
		subject.getTxRecordByContractID(query, queryObserver);

		// expect:
		verify(contractAnswers).getContractRecords();
		verify(queryResponseHelper).answer(query, queryObserver, null, ContractGetRecords);
	}

	@Test
	public void forwardsDelete() {
		// when:
		subject.deleteContract(txn, txnObserver);

		// expect:
		verify(txnResponseHelper).submit(txn, txnObserver, ContractDelete);
	}

	@Test
	public void forwardsSystemDelete() {
		// when:
		subject.systemDelete(txn, txnObserver);

		// expect:
		verify(txnResponseHelper).submit(txn, txnObserver, SystemDelete);
	}

	@Test
	public void forwardsSystemUndelete() {
		// when:
		subject.systemUndelete(txn, txnObserver);

		// expect:
		verify(txnResponseHelper).submit(txn, txnObserver, SystemUndelete);
	}
}
