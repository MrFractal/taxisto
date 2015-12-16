#import "NSMutableArray.h"
#import "NSMutableArray.h"

/**
 * Created by max on 06.02.14.
 */

@interface StringsListResponse : NSObject {
  NSMutableArray * values;
}

@property(nonatomic, retain, readonly) NSMutableArray * values;
- (void) init;
@end
