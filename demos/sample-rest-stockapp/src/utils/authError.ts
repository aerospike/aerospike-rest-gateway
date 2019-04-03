
export default function isAuthError(err?: any): boolean {
    return (err && 'status' in err && err.status === 401);
}