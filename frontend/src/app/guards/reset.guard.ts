import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const resetGuard: CanActivateFn = (route, state) => {
  if(sessionStorage.getItem('reset_token')) {
    return true
  } else {
    const router = inject(Router)
    router.navigate(['login'])
    return false
  }
};
