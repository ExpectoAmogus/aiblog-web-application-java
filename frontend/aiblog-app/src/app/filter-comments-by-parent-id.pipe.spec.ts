import { FilterCommentsByParentIdPipe } from './filter-comments-by-parent-id.pipe';

describe('FilterCommentsByParentIdPipe', () => {
  it('create an instance', () => {
    const pipe = new FilterCommentsByParentIdPipe();
    expect(pipe).toBeTruthy();
  });
});
