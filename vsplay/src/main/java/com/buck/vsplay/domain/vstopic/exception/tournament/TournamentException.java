package com.buck.vsplay.domain.vstopic.exception.tournament;

import com.buck.vsplay.global.exception.BaseException;

public class TournamentException extends BaseException {
    public TournamentException(TournamentExceptionCode tournamentExceptionCode) {
        super(tournamentExceptionCode);
    }
}
