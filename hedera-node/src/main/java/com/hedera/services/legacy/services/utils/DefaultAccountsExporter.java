package com.hedera.services.legacy.services.utils;

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

import com.hedera.services.state.merkle.MerkleEntityId;
import com.hedera.services.legacy.initialization.ExportExistingAccounts;
import com.hedera.services.state.exports.AccountsExporter;
import com.hedera.services.state.merkle.MerkleAccount;
import com.swirlds.fcmap.FCMap;

public class DefaultAccountsExporter implements AccountsExporter {
	@Override
	public void toFile(FCMap<MerkleEntityId, MerkleAccount> accounts, String path) throws Exception {
		ExportExistingAccounts.exportAccounts(path, accounts);
	}
}
