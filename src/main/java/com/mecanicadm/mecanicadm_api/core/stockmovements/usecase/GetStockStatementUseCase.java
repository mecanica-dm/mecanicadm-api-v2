package com.mecanicadm.mecanicadm_api.core.stockmovements.usecase;

import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.dto.StockStatementDTO;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.query.GetStockStatementQuery;

public interface GetStockStatementUseCase {
    StockStatementDTO handle(GetStockStatementQuery query);
}
